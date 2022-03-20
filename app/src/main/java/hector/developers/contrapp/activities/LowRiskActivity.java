package hector.developers.contrapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import hector.developers.contrapp.R;


public class LowRiskActivity extends AppCompatActivity {
//    TextView mTollFree;
    Button mLowRiskCloseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_risk);


        mLowRiskCloseBtn = findViewById(R.id.lowRiskCloseBtn);

        mLowRiskCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LowRiskActivity.this, UserHealthDataActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
