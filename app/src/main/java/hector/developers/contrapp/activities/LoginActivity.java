package hector.developers.contrapp.activities;

import static hector.developers.contrapp.constant.Constants.UPDATE_LOCATION_WORKER;
import static hector.developers.contrapp.constant.Constants.USER_JSON_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.multidex.BuildConfig;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import hector.developers.contrapp.Api.RetrofitClient;
import hector.developers.contrapp.R;
import hector.developers.contrapp.model.Users;
import hector.developers.contrapp.utils.SessionManagement;
import hector.developers.contrapp.utils.Utils;
import hector.developers.contrapp.workers.LocationWorker;
import hector.developers.contrapp.workers.WorkerUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Configuration.Provider {
    private static final String TAG = "LocationUpdate";
    EditText mEmail, mPassword;
    Button mLogin;
    ImageButton mBtnRegisterLink;
    ProgressDialog progressDialog;
    private Utils utils;
    private SessionManagement sessionManagement;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mLogin = findViewById(R.id.btn_login);
        mBtnRegisterLink = findViewById(R.id.btnRegisterLink);

        HashMap<String, String> status = status();
        String statuss = status.get("status");
        utils = new Utils();

        sessionManagement = new SessionManagement(this);
        sessionManagement.getLoginEmail();
        sessionManagement.getLoginPassword();

        if (statuss != null) {

        } else {
            showCustomDialog();
        }

        mBtnRegisterLink.setOnClickListener(v -> {
            Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(regIntent);
            finish();
        });
        mLogin.setOnClickListener(v -> {
            final String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            //validating fields
            if (utils.isNetworkAvailable(getApplicationContext())) {
                if (email.isEmpty()) {
                    mEmail.setError("Email is required");
                    mEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Enter a valid email!");
                    mEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    mPassword.setError("Enter Password!");
                    mPassword.requestFocus();
                    return;
                }
                loginUser(email, password);
            } else {
                Toast.makeText(LoginActivity.this, "Please Check your network connection...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginUser(String email, String password) {
        //do registration API call
        Call<Users> call = RetrofitClient
                .getInstance()
                .getApi().login(email, password);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating Please Wait...");
        progressDialog.show();

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {

                    Users users = response.body();
                    System.out.println("users ..... " + users);
                    Log.d("user33", "response:..  " + new Gson().toJson(response.body()));
                    String phone = response.body().getPhone();
                    System.out.println("phone " + phone);
                    String email = response.body().getEmail();
                    System.out.println("email " + email);
                    String state = response.body().getState();
                    System.out.println("state " + state);

                    System.out.println("users XXXXXX " + response);
                    assert response.body() != null;
//                    userID = response.body().getId();
                    assert users != null;
                    //save userId to shared pref
                    if (users.getUserType().equalsIgnoreCase("admin")) {
                        Intent adminIntent = new Intent(LoginActivity.this, MainActivity.class);
                        assert response.body() != null;
                        sessionManagement.setLoginEmail(email);
                        sessionManagement.setLoginPassword(password);
                        adminIntent.putExtra("Admin", response.body().getUserType());
                        startActivity(adminIntent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Welcome Admin!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    } else {
                        //start worker here after successful login
                        initLocationWorkerRequest(users);

                        Intent intent = new Intent(LoginActivity.this, UserHealthDataActivity.class);
                        intent.putExtra("userId ", users.getId());
                        saveUserId(users.getId());
                        saveUserFirstname(users.getFirstname());
                        saveUserPhone(users.getPhone());
                        System.out.println("users ,,,," + users.getId());
                        assert response.body() != null;
                        intent.putExtra("User", response.body().getUserType());
                        intent.putExtra("state", response.body().getState());
                        startActivity(intent);
                        sessionManagement.setLoginEmail(email);
                        sessionManagement.setLoginPassword(password);
                        Toast.makeText(LoginActivity.this, "Welcome User!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                    System.out.println("RESPONSE ==>> " + response);
                    Toast.makeText(LoginActivity.this, "Cannot Login! Check Credentials..!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Throwing >>>>" + t);
            }
        });
    }

    public void saveUserFirstname(String firstname) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("userFirstname", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("userFirstname", firstname + "");
        edit.apply();
    }

    public void saveUserPhone(String phone) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("userPhone", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("userPhone", phone + "");
        edit.apply();
    }

    public void saveUserId(Long id) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.putString("userId", id + "");
        edit.apply();
    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewGroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        Button ok = dialogView.findViewById(R.id.buttonOk);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStatus();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public HashMap<String, String> status() {
        HashMap<String, String> user = new HashMap<>();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("status", Context.MODE_PRIVATE);
        user.put("status", sharedPreferences.getString("status", null));
        return user;
    }

    @SuppressLint("ApplySharedPref")
    public void saveStatus() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status", "1");
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        }
    }


    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int fineLocation = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocation = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return fineLocation == PackageManager.PERMISSION_GRANTED &&
                coarseLocation == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    view -> {
                        // Request permission
                        ActivityCompat.requestPermissions(LoginActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_PERMISSIONS_REQUEST_CODE);
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
//                initLocationWorkerRequest();
            } else {
                // Permission denied.

                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .build();
    }

    /** create the workerRequest to periodically handle the location
     * updating every 15 minutes as a background service
     * @param user
     * an instance of the logged in user.
    */
    private void initLocationWorkerRequest(Users user) {
        String userJson = Utils.serializeUserToJson(user);
        Data.Builder builder = new Data.Builder();
        builder.putString(USER_JSON_KEY, userJson);
        Data data = builder.build();
        PeriodicWorkRequest updateLocationRequest =
                new PeriodicWorkRequest.Builder(LocationWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(WorkerUtils.networkConstraint())
                        .addTag(TAG)
                        .setInputData(data)
                        .build();

        //start the workerRequest
        WorkManager workManager = WorkManager.getInstance(this.getApplicationContext());
        workManager.enqueueUniquePeriodicWork
                (UPDATE_LOCATION_WORKER, ExistingPeriodicWorkPolicy.REPLACE, updateLocationRequest);
    }
}