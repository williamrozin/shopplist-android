package com.example.william.shopplist.activities.lists;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.william.shopplist.R;
import com.example.william.shopplist.adapter.ListItemAdapter;
import com.example.william.shopplist.model.ListItem;
import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by william on 11/11/17.
 */
public class SelectedListActivity extends AppCompatActivity implements Serializable{
    static ArrayAdapter<ListItem> adapter;
    static ServerInterface server;
    static ListView listItems;
    static ShoppingList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        list = (ShoppingList) i.getSerializableExtra("list");
        server = ServerConnection.getInstance().getServer();

        adapter = new ListItemAdapter(this, android.R.layout.simple_list_item_1,new ArrayList<ListItem>());
        setContentView(R.layout.selected_list);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(list.getDescription());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        adapter.addAll(list.getItems());

        listItems = (ListView) findViewById(R.id.list);
        listItems.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newActivity = new Intent(SelectedListActivity.this, EditListActivity.class);
                newActivity.putExtra("list", list);
                startActivity(newActivity);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        refreshSList(list.getId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selected_list, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = getIntent();
        final ShoppingList list = (ShoppingList) intent.getSerializableExtra("list");

        String message = "Lista de compras " + list.getDescription() + ", " + list.getDate().toString();
        message+= "\n";
        for(int i=0; i < adapter.getCount(); i++) {
            ListItem li = adapter.getItem(i);
            message+= "\n";
            message+= li.getDescription() + (li.isChecked() ? " - ok" : "");
        }

        switch (item.getItemId()) {
            case R.id.share:
                Intent  i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(i,"Compartilhe esta lista com os seus amigos!"));

                break;
            case R.id.delete:
                new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Remover lista")
                    .setMessage("Tem certeza que deseja remover esta lista?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeList(list.getId());
                        }

                    })
                    .setNegativeButton("Não", null)
                    .show();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void removeList(long id) {
        server = ServerConnection.getInstance().getServer();

        Call<Void> request = server.removeList(id);

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

    public void refreshSList(long id) {
        Call<ShoppingList> request = server.getList(id);


        request.enqueue(new Callback<ShoppingList>() {
            @Override
            public void onResponse(Call<ShoppingList> call, Response<ShoppingList> response) {
                ShoppingList refreshedList = response.body();

                getSupportActionBar().setTitle(refreshedList.getDescription());
                list.setDescription(refreshedList.getDescription());
                adapter.clear();
                adapter.addAll(refreshedList.getItems());
            }

            @Override
            public void onFailure(Call<ShoppingList> call, Throwable t) {
                Toast.makeText(SelectedListActivity.this, "Ocorreu um erro ao atualizar a lista",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
