package hector.developers.contrapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import hector.developers.contrapp.Api.RetrofitClient;
import hector.developers.contrapp.R;
import hector.developers.contrapp.model.UserHealthData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHealthDataActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    final Calendar cldr = Calendar.getInstance();
    EditText mDate;

    //global variables
    AtomicInteger sectionOneYes = new AtomicInteger();
    //declaring the radio group;
    AtomicInteger sectionTwoYes = new AtomicInteger();
    //local variables
    ArrayList<String> sectionOne = new ArrayList<>();
    ArrayList<String> sectionTwo = new ArrayList<>();

    RadioGroup mFever, mHeadache, mSneezing, mChestPain,
            mBodyPain, mNauseaOrVomitingSymptom, mDiarrhoea,
            mFlu, mSoreThroatSymptoms, mFatigueSymptom,
            mNewOrWorseningCough, mDifficultyInBreathing,
            mLossOfSmellSymptoms, mLossOfTasteSymptoms, mContactWithFamily;

    Button mBtn_submit;

    private ProgressDialog loadingBar;
    private List<String> states;
    private String userId, userPhone, userFirstname;

    private String risk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_health_data);
        loadingBar = new ProgressDialog(this);

        HashMap<String, String> id = getUserId();
        userId = id.get("userId");

        HashMap<String, String> phone = getUserPhone();
        userPhone = phone.get("userPhone");

        HashMap<String, String> firstname = getUserFirstname();
        userFirstname = firstname.get("userFirstname");

        mDate = findViewById(R.id.dateOfEntry);
        //section1
        mFever = findViewById(R.id.radio_fever);

        mHeadache = findViewById(R.id.radio_headache);
        mSneezing = findViewById(R.id.radio_sneezingSymptoms);
        mChestPain = findViewById(R.id.radio_chestPainSymptoms);
        mBodyPain = findViewById(R.id.radio_bodyPainSymptom);
        mNauseaOrVomitingSymptom = findViewById(R.id.radio_nauseaSymptom);
        mDiarrhoea = findViewById(R.id.radio_diarrhoeaSymptoms);
        mFlu = findViewById(R.id.radio_fluSymptoms);
        mSoreThroatSymptoms = findViewById(R.id.radio_soreSymptoms);
        mFatigueSymptom = findViewById(R.id.fatigueSymptoms);
        //section 2
        mNewOrWorseningCough = findViewById(R.id.cough);
        mDifficultyInBreathing = findViewById(R.id.radio_difficultBreathing);
        mLossOfSmellSymptoms = findViewById(R.id.lossOfSmellSymptoms);
        mLossOfTasteSymptoms = findViewById(R.id.lossTasteSymptoms);
        //section 3
        mContactWithFamily = findViewById(R.id.radio_contactWithFamily);

        mBtn_submit = findViewById(R.id.btn_submit);
//        mUserId = findViewById(R.id.userId);

//        mFever.clearCheck();

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog datePicker = new DatePickerDialog(UserHealthDataActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear = monthOfYear + 1;
                                mDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                            }
                        }, year, month, day);
                //disable future dates
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
                //disable past dates
                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                // show date dialog
                datePicker.show();
            }
        });

        mBtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = mDate.getText().toString().trim();

                final String feverSymptom = ((RadioButton) findViewById(mFever.getCheckedRadioButtonId())).getText().toString();
                final String headacheSymptom = ((RadioButton) findViewById(mHeadache.getCheckedRadioButtonId())).getText().toString();
                final String sneezingSymptoms = ((RadioButton) findViewById(mSneezing.getCheckedRadioButtonId())).getText().toString();
                final String chestPainSymptoms = ((RadioButton) findViewById(mChestPain.getCheckedRadioButtonId())).getText().toString();
                final String bodyPainSymptoms = ((RadioButton) findViewById(mBodyPain.getCheckedRadioButtonId())).getText().toString();
                final String nauseaOrVomitingSymptom = ((RadioButton) findViewById(mNauseaOrVomitingSymptom.getCheckedRadioButtonId())).getText().toString();
                final String diarrhoeaSymptoms = ((RadioButton) findViewById(mDiarrhoea.getCheckedRadioButtonId())).getText().toString();
                final String fluSymptoms = ((RadioButton) findViewById(mFlu.getCheckedRadioButtonId())).getText().toString();
                final String soreThroatSymptoms = ((RadioButton) findViewById(mSoreThroatSymptoms.getCheckedRadioButtonId())).getText().toString();
                final String fatigueSymptoms = ((RadioButton) findViewById(mFatigueSymptom.getCheckedRadioButtonId())).getText().toString();

                final String newOrWorseningCough = ((RadioButton) findViewById(mNewOrWorseningCough.getCheckedRadioButtonId())).getText().toString();
                final String difficultyInBreathingSymptom = ((RadioButton) findViewById(mDifficultyInBreathing.getCheckedRadioButtonId())).getText().toString();
                final String lossOfSmellSymptoms = ((RadioButton) findViewById(mLossOfSmellSymptoms.getCheckedRadioButtonId())).getText().toString();
                final String lossOfTasteSymptoms = ((RadioButton) findViewById(mLossOfTasteSymptoms.getCheckedRadioButtonId())).getText().toString();

                final String contactWithFamily = ((RadioButton) findViewById(mContactWithFamily.getCheckedRadioButtonId())).getText().toString();

                //validations of fields

                if (TextUtils.isEmpty(date)) {
                    mDate.setError("Enter date!");
                    mDate.requestFocus();
                    return;
                }
                //add section 1 results to the arrayList
                sectionOne.add(feverSymptom);
                sectionOne.add(headacheSymptom);
                sectionOne.add(chestPainSymptoms);
                sectionOne.add(sneezingSymptoms);
                sectionOne.add(chestPainSymptoms);
                sectionOne.add(bodyPainSymptoms);
                sectionOne.add(nauseaOrVomitingSymptom);
                sectionOne.add(diarrhoeaSymptoms);
                sectionOne.add(fluSymptoms);
                sectionOne.add(soreThroatSymptoms);
                sectionOne.add(fatigueSymptoms);

                //add section 2 results to the arrayList
                sectionTwo.add(newOrWorseningCough);
                sectionTwo.add(difficultyInBreathingSymptom);
                sectionTwo.add(lossOfSmellSymptoms);
                sectionTwo.add(lossOfTasteSymptoms);
                sectionTwo.add(contactWithFamily);

                //call the method for each section
                checkSectionCount(sectionOne, sectionOneYes);
                checkSectionCount(sectionTwo, sectionTwoYes);


                System.out.println("SECTION ONE ==>>> " + sectionOne);
                System.out.println("SECTION ONE YESSES ==>>> " + sectionOneYes);

                System.out.println("SECTION TWO ==>>> " + sectionTwo);
                System.out.println("SECTION TWO YESSES ==>>> " + sectionTwoYes);


                if (sectionOneYes.get() >= 2 || sectionTwoYes.get() > 0) {
                    System.out.println("RISK IS HIGH");
                    risk = "High Risk";
                } else if (sectionOneYes.get() > 1 && sectionTwoYes.get() > 0) {
                    System.out.println("RISK IS HIGH");
                    risk = "High Risk";
                } else {
                    System.out.println("RISK IS LOW");
                    risk = "Low Risk";
                }

                mFever.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton rbFever = group.findViewById(checkedId);
                        if (rbFever == null && checkedId == -1) {
                            Toast.makeText(getApplicationContext(), "Select an option!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                registerUserHealthData(date, userPhone, feverSymptom,
                        headacheSymptom, sneezingSymptoms, chestPainSymptoms, bodyPainSymptoms,
                        nauseaOrVomitingSymptom, diarrhoeaSymptoms, fluSymptoms, soreThroatSymptoms,
                        fatigueSymptoms, newOrWorseningCough, difficultyInBreathingSymptom,
                        lossOfSmellSymptoms, lossOfTasteSymptoms, contactWithFamily,
                        Long.parseLong(userId), userFirstname, risk
                );
            }
        });


    }


    public void registerUserHealthData(String date, String userPhone,
                                       String feverSymptom,
                                       String headacheSymptom,
                                       String sneezingSymptom,
                                       String chestPainSymptom,
                                       String bodyPainSymptoms,
                                       String nauseaOrVomitingSymptom,
                                       String diarrhoeaSymptom,
                                       String fluSymptom, String soreThroatSymptom,
                                       String fatigueSymptom, String newOrWorseningCough,
                                       String difficultyInBreathing,
                                       String lossOfOrDiminishedSenseOfSmell,
                                       String lossOfOrDiminishedSenseOfTaste,
                                       String contactWithFamily, Long userId,String userFirstname, String risk) {
        //making api calls
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Data...");
        progressDialog.show();

        //building retrofit object
        Call<UserHealthData> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUserHealthData(
                        date, feverSymptom,
                        headacheSymptom, sneezingSymptom,
                        chestPainSymptom,
                        bodyPainSymptoms,
                        nauseaOrVomitingSymptom,
                        diarrhoeaSymptom,
                        fluSymptom, soreThroatSymptom,
                        fatigueSymptom, newOrWorseningCough,
                        difficultyInBreathing,
                        lossOfOrDiminishedSenseOfSmell,
                        lossOfOrDiminishedSenseOfTaste,
                        contactWithFamily,
                        userId, userPhone,userFirstname, risk);
        call.enqueue(new Callback<UserHealthData>() {
            @Override
            public void onResponse(Call<UserHealthData> call, Response<UserHealthData> response) {
                if (response.isSuccessful()) {
                    if (risk.equals("High Risk")) {
                        loadingBar.dismiss();
                        Toast.makeText(UserHealthDataActivity.this, "Data Saved!", Toast.LENGTH_LONG).show();
                        Intent highRiskIntent = new Intent(UserHealthDataActivity.this, HighRiskActivity.class);
                        highRiskIntent.putExtra("risk", "High risk");
                        startActivity(highRiskIntent);
                        finish();
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(UserHealthDataActivity.this, "Data Saved!", Toast.LENGTH_LONG).show();
                        Intent lowRiskIntent = new Intent(UserHealthDataActivity.this, LowRiskActivity.class);
                        startActivity(lowRiskIntent);
                        finish();
                    }
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(UserHealthDataActivity.this, "Data saving failed!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserHealthData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserHealthDataActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public HashMap<String, String> getUserId() {
        HashMap<String, String> id = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userId", Context.MODE_PRIVATE);
        id.put("userId", sharedPreferences.getString("userId", null));
        return id;
    }

    public HashMap<String, String> getUserPhone() {
        HashMap<String, String> phone = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userPhone", Context.MODE_PRIVATE);
        phone.put("userPhone", sharedPreferences.getString("userPhone", null));
        return phone;
    }

    public HashMap<String, String> getUserFirstname() {
        HashMap<String, String> firstname = new HashMap<>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userFirstname", Context.MODE_PRIVATE);
        firstname.put("userFirstname", sharedPreferences.getString("userFirstname", null));
        return firstname;
    }

    //method that should be outside the onCreate(){}
    private void checkSectionCount(ArrayList<String> section, AtomicInteger count) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            section.forEach(symptom -> {
                if (symptom.equalsIgnoreCase("Yes")) {
                    count.getAndIncrement();
                }
            });
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(UserHealthDataActivity.this, LoginActivity.class);
                        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }
}