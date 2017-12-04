package com.example.william.shopplist.activities.categories;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.william.shopplist.R;
import com.example.william.shopplist.activities.categories.EditCategoryActivity;
import com.example.william.shopplist.model.Category;
import com.example.william.shopplist.model.ListItem;
import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;

import org.w3c.dom.Text;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by william on 11/11/17.
 */
public class SelectedCategoryActivity extends AppCompatActivity implements Serializable{
    static ServerInterface server;
    static Category category;
    static TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        category = (Category) i.getSerializableExtra("category");
        server = ServerConnection.getInstance().getServer();

        setContentView(R.layout.selected_category);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Categoria");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        description = (TextView) findViewById(R.id.editText);
        description.setText(category.getDescription());
        description.setTextColor(Color.parseColor(category.getColor()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);

        if (category.getUserId() == 0) {
            fab.setVisibility(View.GONE);
        } else {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newActivity = new Intent(SelectedCategoryActivity.this, EditCategoryActivity.class);
                    newActivity.putExtra("category", category);
                    startActivity(newActivity);
                }
            });
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        refreshCategory(category.getId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (category.getUserId() != 0) {
            getMenuInflater().inflate(R.menu.menu_selected_category, menu);
        }
        return super.onCreateOptionsMenu(menu);

    }



    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = getIntent();

        if (item.getItemId() == R.id.delete) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Remover categoria")
                    .setMessage("Tem certeza que deseja remover esta categoria?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeCategory(category.getId());
                        }

                    })
                    .setNegativeButton("Não", null)
                    .show();
        }

        return super.onOptionsItemSelected(item);

    }
    public void removeCategory(long id) {
        server = ServerConnection.getInstance().getServer();

        Call<Void> request = server.removeCategory(id);

        request.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                onBackPressed();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("DSI2017", "deu erro meu chapa");
            }
        });
    }

    public void refreshCategory(long id) {
        Call<Category> request = server.getCategory(id);


        request.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                Category refreshedCategory = response.body();

                getSupportActionBar().setTitle(refreshedCategory.getDescription());
                category.setDescription(refreshedCategory.getDescription());
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(SelectedCategoryActivity.this, "Ocorreu um erro ao atualizar a categoria",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
