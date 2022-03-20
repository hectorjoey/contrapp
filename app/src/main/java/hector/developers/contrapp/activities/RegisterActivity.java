package hector.developers.contrapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ikhiloyaimokhai.nigeriastatesandlgas.Nigeria;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hector.developers.contrapp.Api.RetrofitClient;
import hector.developers.contrapp.R;
import hector.developers.contrapp.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final int SPINNER_HEIGHT = 500;
    EditText mFirstname, mLastname, mEmail, mPassword, mNin, mPhone, mAddress, mNextOfKin, mRelationShipWithNextOfKin, mNextOfKinPhone;
    Button mButtonRegister;
    Spinner mStateSpinner, mLgaSpinner, mAgeSpinner, mGender;
    private String mState, mLga;
    private List<String> states;

    private Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirstname = findViewById(R.id.firstname);
        mLastname = findViewById(R.id.lastname);
        mPhone = findViewById(R.id.phone);
        mAddress = findViewById(R.id.address);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        mAgeSpinner = findViewById(R.id.ageSpinner);
        mGender = findViewById(R.id.genderSpinner);
        mNin = findViewById(R.id.nin);
        mNextOfKin = findViewById(R.id.nextOfKin);
        mRelationShipWithNextOfKin = findViewById(R.id.relationshipWithNextOfKin);
        mNextOfKinPhone = findViewById(R.id.nextOfKinPhone);

        mButtonRegister = findViewById(R.id.btnRegister);

        utils = new Utils();

        resizeSpinner(mStateSpinner, SPINNER_HEIGHT);
        states = Nigeria.getStates();
//        call to method that'll set up state and lga spinner
        setupSpinners();
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname = mFirstname.getText().toString().trim();
                final String lastname = mLastname.getText().toString().trim();
                final String nin = mNin.getText().toString().trim();
                final String gender = mGender.getSelectedItem().toString();
                final String address = mAddress.getText().toString().trim();
                final String state = mStateSpinner.getSelectedItem().toString();
                final String age = mAgeSpinner.getSelectedItem().toString();
                final String nextOfKin = mNextOfKin.getText().toString().trim();
                final String relationShipWithNextOfKin = mRelationShipWithNextOfKin.getText().toString().trim();
                final String nextOfKinPhone = mNextOfKinPhone.getText().toString().trim();

                final String lga = mLgaSpinner.getSelectedItem().toString();
                final String email = mEmail.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();

                //validating fields
                if (utils.isNetworkAvailable(getApplicationContext())) {
                    if (TextUtils.isEmpty(firstname)) {
                        mFirstname.setError("first name is required!");
                        mFirstname.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(lastname)) {
                        mLastname.setError("last name is required!");
                        mLastname.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(phone)) {
                        mPhone.setError("Phone number is required!");
                        mPhone.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(email)) {
                        mEmail.setError("Email is required!");
                        mEmail.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(address)) {
                        mAddress.setError("Address is required!");
                        mAddress.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(nextOfKin)) {
                        mNextOfKin.setError("Next of kin is required!");
                        mNextOfKin.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(relationShipWithNextOfKin)) {
                        mRelationShipWithNextOfKin.setError("Relationship with Next of kin is required!");
                        mRelationShipWithNextOfKin.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(nextOfKinPhone)) {
                        mNextOfKinPhone.setError("Next of kin phone number is required!");
                        mNextOfKinPhone.requestFocus();
                        return;
                    }

                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        mEmail.setError("Enter a valid email!");
                        mEmail.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        mPassword.setError("Password is required!");
                        mPassword.requestFocus();
                        return;
                    }
                    if (password.length() < 6) {
                        mPassword.setError("Password length is too short!");
                        mPassword.requestFocus();
                    }
                    if (TextUtils.isEmpty(nin)) {
                        mNin.setError("Designation is required!");
                    }


                    registerUser(firstname, lastname, age, phone, email, gender, address, password, state, lga, nin,
                            nextOfKin, relationShipWithNextOfKin, nextOfKinPhone);

                } else {
                    Toast.makeText(RegisterActivity.this, "Please Check your network connection...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mGender.setAdapter(genderAdapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mAgeSpinner.setAdapter(ageAdapter);

    }

    /**
     * Method to set up the spinners
     */
    public void setupSpinners() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        //populates the quantity spinner ArrayList
        ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);

        // Specify dropdown layout style - simple list view with 1 item per line
        statesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        statesAdapter.notifyDataSetChanged();
        mStateSpinner.setAdapter(statesAdapter);

        // Set the integer mSelected to the constant values
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mState = (String) parent.getItemAtPosition(position);
                setUpStatesSpinner(position);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Unknown
            }
        });
    }

    /**
     * method to set up the state spinner
     *
     * @param position current position of the spinner
     */
    private void setUpStatesSpinner(int position) {
        List<String> list = new ArrayList<>(Nigeria.getLgasByState(states.get(position)));
        setUpLgaSpinner(list);
    }

    /**
     * Method to set up the local government areas corresponding to selected states
     *
     * @param lgas represents the local government areas of the selected state
     */
    private void setUpLgaSpinner(List<String> lgas) {
        ArrayAdapter lgaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lgas);
        lgaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        lgaAdapter.notifyDataSetChanged();
        mLgaSpinner.setAdapter(lgaAdapter);

        mLgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mLga = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void resizeSpinner(Spinner spinner, int height) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            //Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            //set popupWindow height to height
            popupWindow.setHeight(height);

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void registerUser(String firstname, String lastname, String age, String phone, String email, String gender,
                             String address, String password, String state, String lga, String nin, String nextOfKin,
                             String relationShipWithNextOfKin, String nextOfKinPhone) {

        //do registration API call
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(firstname, lastname, age, phone, email, gender, address,
                        password, state, lga, nin, nextOfKin, relationShipWithNextOfKin, nextOfKinPhone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    System.out.println("Responding ::: " + response);
                    clearFields();

                    System.out.println("firstname: " + firstname + " Lastname: " + lastname
                            + " age: " + age + " phone: " + phone + " email: " + email
                            + " gender: " + gender + " address: " + address + " Password:: " + password
                            + " state: " + state + " lga: " + lga + " NIN: " + nin + "next of kin: " +
                            nextOfKin + " relationship: " + relationShipWithNextOfKin
                            + " next of kin " + nextOfKinPhone);
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                    System.out.println("Failed: " + response.body());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                System.out.println("throwing " + t);
            }
        });
    }

    public void clearFields() {
        mEmail.setText("");
        mPassword.setText("");
        mFirstname.setText("");
        mLastname.setText("");
        mNin.setText("");
        mPhone.setText("");
        mNextOfKin.setText("");
        mRelationShipWithNextOfKin.setText("");
        mNextOfKinPhone.setText("");
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Want to go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }
}