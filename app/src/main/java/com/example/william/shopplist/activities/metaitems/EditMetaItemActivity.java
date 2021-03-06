package com.example.william.shopplist.activities.metaitems;

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
import com.example.william.shopplist.model.MetaItem;
import com.example.william.shopplist.model.MetaItemList;
import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.model.User;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by william on 01/12/17.
 */
public class EditMetaItemActivity extends AppCompatActivity {
    ServerInterface server;
    Spinner dropdown;
    Category category;
    MetaItem metaItem;
    ArrayAdapter<Category> adapter;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_metaitem);
        Intent i = getIntent();
        metaItem = (MetaItem) i.getSerializableExtra("metaItem");
        category = metaItem.getCategory();
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);

        server = ServerConnection.getInstance().getServer();

        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Editar produto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        description = (EditText) findViewById(R.id.editText);
        description.setText(metaItem.getDescription());
        dropdown = (Spinner) findViewById(R.id.dropdown);

        adapter = new ArrayAdapter<Category>(EditMetaItemActivity.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<Category>());
        dropdown.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (description.getText().toString().compareTo("") != 0) {
                    metaItem.setDescription(description.getText().toString());
                    metaItem.setCategory(category);
                    updateMetaItem(metaItem);
                } else {
                    Toast.makeText(EditMetaItemActivity.this, "Informe um nome para este produto",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                category = (Category) parent.getItemAtPosition(position);
                Log.i("DSI2017CAT", category.getDescription());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        fillCategories();
    }

    public void fillCategories(){
        Call<List<Category>> request = server.getAllCategories(metaItem.getUserId());

        request.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> listData = response.body();

                if(listData != null) {
                    adapter.clear();
                    adapter.addAll(listData);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.i("DSI2017", "Não deu");
            }
        });
    }

    public void updateMetaItem(MetaItem mi) {
        Call<Void> request = server.updateMetaItem(mi.getId(), mi);

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
