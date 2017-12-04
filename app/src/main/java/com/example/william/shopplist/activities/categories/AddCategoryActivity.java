package com.example.william.shopplist.activities.categories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.william.shopplist.R;
import com.example.william.shopplist.model.Category;
import com.example.william.shopplist.model.ListItem;
import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.model.User;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by william on 01/12/17.
 */
public class AddCategoryActivity extends AppCompatActivity {
    ServerInterface server;
    Spinner dropdown;
    String color;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);

        server = ServerConnection.getInstance().getServer();

        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Adicionar categoria");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dropdown = (Spinner) findViewById(R.id.dropdown);
        String[] items = new String[]{"Vermelho", "Verde", "Azul", "Amarelo", "Alaranjado", "Marrom", "Preto"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        color = "#D32F2F";
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                color = (String) parent.getItemAtPosition(position);

                switch ((String) parent.getItemAtPosition(position)) {
                    case "Verde":
                        color = "#388E3C";
                        break;
                    case "Azul":
                        color = "#1976D2";
                        break;
                    case "Amarelo":
                        color = "#FBC02D";
                        break;
                    case "Alaranjado":
                        color = "#F57C00";
                        break;
                    case "Marrom":
                        color = "#5D4037";
                        break;
                    case "Preto":
                        color = "#000000";
                        break;
                    default:
                        color = "#D32F2F";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText description = (EditText) findViewById(R.id.editText);

                Category cat = new Category();
                cat.setDescription(description.getText().toString());
                cat.setUserId(user.getId());
                cat.setColor(color);

                if (description.getText().toString().compareTo("") != 0) {
                    createCategory(cat);
                } else {
                    Toast.makeText(AddCategoryActivity.this, "Informe um nome para esta lista",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void createCategory(Category cat) {
        Call<Void> request = server.createCategory(cat);


        request.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                onBackPressed();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("DSI2017", "Não deu");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
