package hector.developers.contrapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hector.developers.contrapp.Api.RetrofitClient;
import hector.developers.contrapp.R;
import hector.developers.contrapp.activities.LoginActivity;
import hector.developers.contrapp.activities.MainActivity;
//import hector.developers.contrapp.adapter.UserAdapter;
import hector.developers.contrapp.model.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardActivity extends AppCompatActivity {
    ProgressDialog loadingBar;
    private RecyclerView rv;
    private List<Users> userList;
//    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        rv = findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        loadingBar = new ProgressDialog(this);
        fetchData();
    }


    private void fetchData() {
        Call<List<Users>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUsers();
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                loadingBar.dismiss();
                userList = response.body();
                System.out.println("Respondingg >>>>" + userList);
                System.out.println("Respondingg ++++++" + response);
//                adapter = new UserAdapter((ArrayList<Users>) userList, DashboardActivity.this);
//                System.out.println(adapter);
//                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }
}