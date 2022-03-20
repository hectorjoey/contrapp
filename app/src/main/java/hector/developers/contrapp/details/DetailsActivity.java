package hector.developers.contrapp.details;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import hector.developers.contrapp.R;


public class DetailsActivity extends AppCompatActivity {
    String mFirstName, mLastName, mState, mDesignation, mUserType,
            mStatus;
    CardView cardView;
    TextView firstname, lastname, state, designation, userType, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        firstname = findViewById(R.id.detailsFirstname);
        lastname = findViewById(R.id.detailsLastname);
        state = findViewById(R.id.detailsState);
        designation = findViewById(R.id.detailsDesignation);
        userType = findViewById(R.id.detailsUserType);
        status = findViewById(R.id.detailsStatus);

        displayDetails();
    }

    private void displayDetails() {
        mFirstName = getIntent().getStringExtra("firstname");
        mLastName = getIntent().getStringExtra("lastname");
        mDesignation = getIntent().getStringExtra("designation");

    }
}