package com.example.william.shopplist.activities.lists;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.william.shopplist.R;
import com.example.william.shopplist.adapter.MetaItemAdapter;
import com.example.william.shopplist.model.Category;
import com.example.william.shopplist.model.ListItem;
import com.example.william.shopplist.model.MetaItem;
import com.example.william.shopplist.model.MetaItemList;
import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by william on 11/11/17.
 */
public class EditCategoryActivity extends AppCompatActivity {
    MetaItemAdapter metaItemAdapter;
    ServerInterface server;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        category = (Category) i.getSerializableExtra("category");
        server = ServerConnection.getInstance().getServer();
        setContentView(R.layout.edit_category);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Editar categoria");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        EditText description = (EditText) findViewById(R.id.editText);
        description.setText(category.getDescription());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_createList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText description = (EditText) findViewById(R.id.editText);
                category.setDescription(description.getText().toString());

                if (description.getText().toString().compareTo("") != 0) {
                    updateCategory(category);
                } else {
                    Toast.makeText(EditCategoryActivity.this, "Informe um nome para esta categoria",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateCategory(Category cat) {
        Call<Void> request = server.updateCategory(cat.getId(), cat);


        request.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                onBackPressed();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditCategoryActivity.this, "Ocorreu um erro ao editar a categoria",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
