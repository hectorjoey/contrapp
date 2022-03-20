package hector.developers.contrapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import hector.developers.contrapp.R;
import hector.developers.contrapp.list.HealthListActivity;
import hector.developers.contrapp.list.UserListActivity;


public class MainActivity extends AppCompatActivity {
    MaterialCardView mViewResult, mViewUsers, mUsersOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewResult = findViewById(R.id.viewReultCardId);
        mViewUsers = findViewById(R.id.viewUsersCardId);
        mUsersOnMap = findViewById(R.id.viewUsersOnMapCardId);

        mViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(MainActivity.this, HealthListActivity.class);
                startActivity(resultIntent);
                finish();
            }
        });

        mViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewUsersIntent = new Intent(MainActivity.this, UserListActivity.class);
                startActivity(viewUsersIntent);
                finish();
            }
        });

    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Want to go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }
}