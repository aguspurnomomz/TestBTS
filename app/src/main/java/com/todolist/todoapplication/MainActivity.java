package com.todolist.todoapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.todolist.todoapplication.api.IAPIService;
import com.todolist.todoapplication.api.UtilsAPI;
import com.todolist.todoapplication.api.model.loginResponseModel;
import com.todolist.todoapplication.dashboard.dashboard;
import com.todolist.todoapplication.databinding.ActivityMainBinding;
import com.todolist.todoapplication.register.registerActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private String message;
    private Integer status;

    private String token;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin, btnRegister;
    private TextView register;

    private IAPIService mApiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        txtUsername = findViewById(R.id.textUsername);
        txtPassword = findViewById(R.id.textPassword);
        btnLogin = findViewById(R.id.buttonLogin);

        mApiService = UtilsAPI.getApiService();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                loginUser(username, password);
//
//                if (!username.isEmpty() && !password.isEmpty()) {
//                    loginUser(username, password);
//                    Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        btnRegister = findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent absenpegawai = new Intent(MainActivity.this, registerActivity.class);
                startActivity(absenpegawai);
                finish();
            }
        });
    }

    private void loginUser(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            Call<loginResponseModel> call = mApiService.loginUser(
                    username, password);
            call.enqueue(new Callback<loginResponseModel>() {
                @Override
                public void onResponse(Call<loginResponseModel> call, Response<loginResponseModel> response) {
                    if (response.isSuccessful()) {
                        loginResponseModel apiResponse = response.body();
                        status = apiResponse.getStatusCode();
                        message = apiResponse.getMessage();

                        if (status == 2110) {
                            token = String.valueOf(apiResponse.getData().getToken());

                            Intent intent = new Intent(MainActivity.this, dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putString("token", token);
                            editor.apply();

                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        switch (response.code()) {
                            case 404:
                                Toast.makeText(MainActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(MainActivity.this, "Mohon Coba Beberapa Saat Kembali", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Mohon Coba Beberapa Saat Kembali", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<loginResponseModel> call, Throwable t) {
                    if (MainActivity.this != null) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                final Toast toast = Toast.makeText(MainActivity.this, "Coba beberapa saat kembali!", Toast.LENGTH_SHORT );
                                toast.show();
                            }
                        });
                    } else {
                        Log.d("onFailure", "Context is: " + MainActivity.this);
                    }
                }
            });
        } else if (!username.isEmpty()) {
            txtPassword.setError("Masukkan Password");

        } else if (!password.isEmpty()) {
            txtUsername.setError("Masukkan Username");
        }
    }
}