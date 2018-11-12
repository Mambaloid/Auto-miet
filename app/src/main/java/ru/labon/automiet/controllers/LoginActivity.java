package ru.labon.automiet.controllers;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.labon.automiet.App;
import ru.labon.automiet.R;
import ru.labon.automiet.models.ResultAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLogin;
    private EditText editTextPassword;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);

        editTextLogin = (EditText) findViewById(R.id.form_email);
        editTextPassword = (EditText) findViewById(R.id.form_pass);
        Button loginBtn = (Button) findViewById(R.id.sign_up_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            auth();
            }
        });
        dialog = new SpotsDialog(this, "Авторизация..");
    }

    private void auth() {
        String email = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Вы заполнили не все поля!", Toast.LENGTH_LONG).show();
            return;
        }
        dialog.show();
        App.getNetClient().auth(email, password, new Callback<ResultAuth>() {
            @Override
            public void onResponse(Call<ResultAuth> call, Response<ResultAuth> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Авторизация прошла успешно", Toast.LENGTH_LONG).show();
                    App.setAccessToken(response.body().token);
                    App.setSum(response.body().checksum);
                    App.createNetClient();
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                } else {
                    switch (response.code()) {
                        case 400:
                            Toast.makeText(LoginActivity.this, "Неверный E-mail или пароль", Toast.LENGTH_LONG).show();
                            editTextLogin.setText("");
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "Ошибка регистрации", Toast.LENGTH_LONG).show();
                            break;
                    }
                    editTextPassword.setText("");
                }
            }
            @Override
            public void onFailure(Call<ResultAuth> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "Ошибка регистрации", Toast.LENGTH_LONG).show();
                editTextPassword.setText("");
            }
        });
    }



}
