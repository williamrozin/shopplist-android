package com.example.william.shopplist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.william.shopplist.model.User;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by william on 16/11/17.
 */
public class SignupActivity extends AppCompatActivity {
    static ServerInterface server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Window window = SignupActivity.this.getWindow();
        window.setStatusBarColor(Color.parseColor("#283593"));

        TextView login = (TextView) findViewById(R.id.link_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        AppCompatButton button = (AppCompatButton) findViewById(R.id.btn_signup);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.input_name);
                EditText email = (EditText) findViewById(R.id.input_email);
                EditText password = (EditText) findViewById(R.id.input_password);
                User user = new User();
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                user.setName(name.getText().toString());

                server = ServerConnection.getInstance().getServer();

                Call<User> request = server.signup(user);

                request.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null) {
                            User loggedUser = response.body();
                            Intent logged = new Intent(SignupActivity.this, MainActivity.class);
                            logged.putExtra("user", loggedUser);
                            startActivity(logged);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(SignupActivity.this, "Ocorreu um erro no cadastro",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
