package hector.developers.contrapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import hector.developers.contrapp.R;

public class HighRiskActivity extends AppCompatActivity {

    Button mHighRiskCloseBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_risk);
        mHighRiskCloseBtn = findViewById(R.id.highRiskCloseBtn);

        mHighRiskCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighRiskActivity.this, UserHealthDataActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
