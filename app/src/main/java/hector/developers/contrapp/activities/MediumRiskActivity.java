package hector.developers.contrapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import hector.developers.contrapp.R;


public class MediumRiskActivity extends AppCompatActivity {
Button mMediumRiskCloseBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium_risk);
        mMediumRiskCloseBtn = findViewById(R.id.mediumRiskCloseBtn);

        mMediumRiskCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediumRiskActivity.this, UserHealthDataActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
