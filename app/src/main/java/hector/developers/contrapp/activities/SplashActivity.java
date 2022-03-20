package hector.developers.contrapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import hector.developers.contrapp.R;

public class SplashActivity extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, MapActivity.class);
            startActivity(mainIntent);
            finish();
        }, 3000);
    }
}