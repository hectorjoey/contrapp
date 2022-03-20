package hector.developers.contrapp.workers;

import static hector.developers.contrapp.constant.Constants.LOCATION_CREATED;
import static hector.developers.contrapp.constant.Constants.USER_JSON_KEY;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import hector.developers.contrapp.Api.RetrofitClient;
import hector.developers.contrapp.model.UserLocation;
import hector.developers.contrapp.model.Users;
import hector.developers.contrapp.sharedPref.PH;
import hector.developers.contrapp.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationWorker extends Worker {

    private static final String TAG = "LocationWorker";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    /**
     * The current location.
     */
    private Location mLocation;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    private Context mContext;
    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private UserLocation userLocation;

    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String userJson = data.getString(USER_JSON_KEY);
        Users user = Utils.deserializeUserFromJson(userJson);
        Log.d(TAG, "onStartJob: STARTING JOB..");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            mFusedLocationClient
                    .getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLocation = task.getResult();

                            userLocation = getUserLocation(mLocation.getLatitude(), mLocation.getLongitude(), user.getId());

                            //check shared preference, if location has been created before then
                            // make API call either create or update depending on the required action
                            boolean hasLocation = PH.get().getBoolean(mContext, LOCATION_CREATED, false);
                            if (hasLocation) {
//                                update existing location
                                updateLocation(userLocation, user.getId());
                            } else {
//                                create new location
                                createLocation(userLocation);
                                PH.get().setBoolean(mContext, LOCATION_CREATED, true);
                            }


                            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                        } else {
                            Log.w(TAG, "Failed to get location.");
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, null)
                    .addOnSuccessListener(locationUpdate -> {
                        if (locationUpdate == null) {
                            Log.w(TAG, "onSuccess:null");
                            return;
                        }
                        System.out.println("LOCATION UPDATE => " + locationUpdate);
                    });
            Log.d(TAG, "Requesting location update");

        } catch (SecurityException unlikely) {
            //Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }

        return Result.success();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private UserLocation getUserLocation(double LATITUDE, double LONGITUDE, Long userId) {
        UserLocation userLocation = new UserLocation();
        String strAddress = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAddress = strReturnedAddress.toString();
            }

            userLocation.setLatitude(String.valueOf(LATITUDE));
            userLocation.setLongitude(String.valueOf(LONGITUDE));
            userLocation.setAddress(strAddress);
            userLocation.setDate(LocalDateTime.now()
                    .format(DateTimeFormatter
                            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
            userLocation.setUserId(userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userLocation;
    }


    public void createLocation(UserLocation userLocation) {
        System.out.println("WHAT IS LOCATION => "+ userLocation);
        Call<UserLocation> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUserLocation(userLocation);
        makeApiCall(call, "create");
    }

    public void updateLocation(UserLocation userLocation, Long userId) {
        System.out.println("WHAT IS LOCATION => "+ userLocation);
        Call<UserLocation> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateUserLocation(userLocation, userId);
        makeApiCall(call, "update");
    }

    public void makeApiCall(Call<UserLocation> call, String action) {
        call.enqueue(new Callback<UserLocation>() {
            @Override
            public void onResponse(Call<UserLocation> call, @NonNull Response<UserLocation> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: location " + action + " " + response.body());
                } else {
                    Log.d(TAG, "onResponse: couldn't " + action + " location " + response.body());
                }
            }

            @Override
            public void onFailure(Call<UserLocation> call, Throwable t) {
                Log.d(TAG, "onFailure: Retrofit error ", t.getCause());
            }
        });
    }
}