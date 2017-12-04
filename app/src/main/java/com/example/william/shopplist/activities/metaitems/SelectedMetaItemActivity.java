package com.example.william.shopplist.activities.metaitems;

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
import com.example.william.shopplist.activities.metaitems.EditMetaItemActivity;
import com.example.william.shopplist.model.Category;
import com.example.william.shopplist.model.ListItem;
import com.example.william.shopplist.model.MetaItem;
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
public class SelectedMetaItemActivity extends AppCompatActivity implements Serializable{
    static ServerInterface server;
    static MetaItem metaItem;
    static TextView description;
    static TextView category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        metaItem = (MetaItem) i.getSerializableExtra("metaItem");
        server = ServerConnection.getInstance().getServer();

        setContentView(R.layout.selected_metaitem);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Produto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        description = (TextView) findViewById(R.id.editText);
        description.setText(metaItem.getDescription());

        category = (TextView) findViewById(R.id.editText2);
        category.setText(metaItem.getCategory().getDescription());
        category.setTextColor(Color.parseColor(metaItem.getCategory().getColor()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);

        if (metaItem.getUserId() == 0) {
            fab.setVisibility(View.GONE);
        } else {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newActivity = new Intent(SelectedMetaItemActivity.this, EditMetaItemActivity.class);
                    newActivity.putExtra("metaItem", metaItem);
                    startActivity(newActivity);
                }
            });
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        refreshMetaItem(metaItem.getId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (metaItem.getUserId() != 0) {
            getMenuInflater().inflate(R.menu.menu_selected_category, menu);
        }
        return super.onCreateOptionsMenu(menu);

    }



    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = getIntent();

        if (item.getItemId() == R.id.delete) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Remover produto")
                    .setMessage("Tem certeza que deseja remover este produto?")
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

        Call<Void> request = server.removeMetaItem(id);

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

    public void refreshMetaItem(long id) {
        Call<MetaItem> request = server.getMetaItem(id);


        request.enqueue(new Callback<MetaItem>() {
            @Override
            public void onResponse(Call<MetaItem> call, Response<MetaItem> response) {
                MetaItem refreshedMetaItem = response.body();

                metaItem.setDescription(refreshedMetaItem.getDescription());
                description.setText(refreshedMetaItem.getDescription());

                category.setText(refreshedMetaItem.getCategory().getDescription());
                category.setTextColor(Color.parseColor(refreshedMetaItem.getCategory().getColor()));
            }

            @Override
            public void onFailure(Call<MetaItem> call, Throwable t) {
                Toast.makeText(SelectedMetaItemActivity.this, "Ocorreu um erro ao atualizar o produto",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
