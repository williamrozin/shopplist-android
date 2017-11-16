package com.example.william.shopplist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.model.User;
import com.example.william.shopplist.model.Login;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * Created by william on 16/11/17.
 */
public class LoginActivity extends AppCompatActivity {
    static ServerInterface servidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Window window = LoginActivity.this.getWindow();
        window.setStatusBarColor(Color.parseColor("#424242"));

        AppCompatButton button = (AppCompatButton) findViewById(R.id.btn_login);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.input_email);
                EditText password = (EditText) findViewById(R.id.input_password);
                Login login = new Login();
                login.setEmail(email.getText().toString());
                login.setPassword(password.getText().toString());

                servidor = ServerConnection.getInstance().getServidor();

                Call<User> retorno = servidor.login(login);


                retorno.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        if (response.body() != null) {
                            User loggedUser = response.body();
                            Intent logged = new Intent(LoginActivity.this, MainActivity.class);
                            logged.putExtra("user", loggedUser);
                            startActivity(logged);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email ou senha incorretos",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.i("DSI2017", "Não deu");
                    }
                });
            }
        });
    }
}
