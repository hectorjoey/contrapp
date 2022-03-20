package hector.developers.contrapp.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hector.developers.contrapp.R;
import hector.developers.contrapp.model.UserLocation;
import hector.developers.contrapp.model.Users;

/**
 * Created by hector Developers on 3/11/20.
 */

public class Utils {
    public static final String TAG = "APP UTILS";


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectMgr;
        connectMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Method for dismissing the keyboard
    public void dismissKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    public void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    public UserLocation getCurrentLocation(Context context, Location location){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        // Address found using the Geocoder.
        List<Address> addresses = null;
        String errorMessage = "";
        UserLocation userLocation = new UserLocation();
        userLocation.setLongitude(String.valueOf(location.getLongitude()));
        userLocation.setLatitude(String.valueOf(location.getLatitude()));

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = context.getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = context.getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = context.getString(R.string.no_address_found);
                Log.e(TAG, "ERROR IN WORKER UTIL => "+errorMessage);
            }
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, context.getString(R.string.address_found));
            userLocation.setAddress(TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }

        return userLocation;
    }

    public static String serializeUserToJson(Users user){
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    public static Users deserializeUserFromJson(String jsonString){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Users.class);
    }
}