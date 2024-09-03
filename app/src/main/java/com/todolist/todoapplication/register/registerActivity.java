package com.todolist.todoapplication.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.todolist.todoapplication.MainActivity;
import com.todolist.todoapplication.R;
import com.todolist.todoapplication.api.IAPIService;
import com.todolist.todoapplication.api.model.loginResponseModel;
import com.todolist.todoapplication.api.model.registerResponseModel;
import com.todolist.todoapplication.dashboard.dashboard;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class registerActivity extends AppCompatActivity {
    private EditText inputTextUsername;
    private EditText inputTextEmail;
    private EditText inputTextPassword;
    private EditText inputTextConfirmPassword;
    private Button buttonRegister;

    private IAPIService mApiService;

    private String message;
    private Integer status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        inputTextUsername = findViewById(R.id.inputTextUsername);
        inputTextEmail = findViewById(R.id.inputTextEmail);
        inputTextPassword = findViewById(R.id.inputtPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputTextUsername.getText().toString();
                String email = inputTextEmail.getText().toString();
                String password = inputTextPassword.getText().toString();

                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(username, email, password);
                } else {
                    Toast.makeText(registerActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(String username,String email, String password) {
        if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
            Call<registerResponseModel> call = mApiService.registerUser(
                    username,email, password);
            call.enqueue(new Callback<registerResponseModel>() {
                @Override
                public void onResponse(Call<registerResponseModel> call, Response<registerResponseModel> response) {
                    if (response.isSuccessful()) {
                        registerResponseModel apiResponse = response.body();
                        status = apiResponse.getStatusCode();
                        message = apiResponse.getMessage();

                        if (status == 2000) {
                            Toast.makeText(registerActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(registerActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(registerActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        switch (response.code()) {
                            case 404:
                                Toast.makeText(registerActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(registerActivity.this, "Mohon Coba Beberapa Saat Kembali", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(registerActivity.this, "Mohon Coba Beberapa Saat Kembali", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<registerResponseModel> call, Throwable t) {
                    if (registerActivity.this != null) {
                        registerActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                final Toast toast = Toast.makeText(registerActivity.this, "Coba beberapa saat kembali!", Toast.LENGTH_SHORT );
                                toast.show();
                            }
                        });
                    } else {
                        Log.d("onFailure", "Context is: " + registerActivity.this);
                    }
                }
            });
        }
    }
}