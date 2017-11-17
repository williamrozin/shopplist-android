package com.example.william.shopplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.william.shopplist.adapter.MetaItemAdapter;
import com.example.william.shopplist.model.ListItem;
import com.example.william.shopplist.model.MetaItem;
import com.example.william.shopplist.model.MetaItemList;
import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.model.User;
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
public class AddListActivity extends AppCompatActivity {
    static MetaItemAdapter metaItemAdapter;
    static ServerInterface servidor;
    static ListView metaItems;
    static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");

        servidor = ServerConnection.getInstance().getServidor();

        metaItemAdapter = new MetaItemAdapter(this, R.layout.lists_adapter, new ArrayList<MetaItemList>());
        setContentView(R.layout.add_list);

        metaItems = (ListView) findViewById(R.id.addlist_metaitems);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Adicionar lista");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fillMetaItemList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_createList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText description = (EditText) findViewById(R.id.editText);

                ShoppingList sl = new ShoppingList();
                sl.setDescription(description.getText().toString());
                sl.setUserId(user.getId());
                for(int i=0; i < metaItemAdapter.getCount(); i++) {
                    if (metaItemAdapter.getItem(i).isChecked()) {
                        ListItem listItem = new ListItem();
                        listItem.setMetaItem(metaItemAdapter.getItem(i).getMetaItem());
                        if (metaItemAdapter.getItem(i).getMetaItem() != null) {
                            sl.addListItem(listItem);
                        }
                    }
                }

                createShoppingList(sl);
            }
        });
    }

    public static void fillMetaItemList(){
        Call<List<MetaItem>> retorno = servidor.getAllMetaItems(user.getId());

        Log.i("DSI2017","Chamando servidor");

        retorno.enqueue(new Callback<List<MetaItem>>() {
            @Override
            public void onResponse(Call<List<MetaItem>> call, Response<List<MetaItem>> response) {
                List<MetaItem> listData = response.body();

                if(listData != null) {
                    metaItemAdapter.clear();
                    for(int i=0; i < listData.size(); i++) {
                        MetaItemList mi = new MetaItemList();
                        mi.setMetaItem(listData.get(i));
                        mi.unsetChecked();
                        metaItemAdapter.add(mi);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MetaItem>> call, Throwable t) {
                Log.i("DSI2017", "Não deu");
            }
        });
        metaItems.setAdapter(metaItemAdapter);
    }

    public void createShoppingList(ShoppingList list) {
        Call<ShoppingList> retorno = servidor.createShoppingList(list);


        retorno.enqueue(new Callback<ShoppingList>() {
            @Override
            public void onResponse(Call<ShoppingList> call, Response<ShoppingList> response) {

                if (response.body() != null) {
                    ShoppingList responseList = response.body();
                    Log.i("DSI2017", responseList.getDescription());
                    onBackPressed();
                    finish();
                } else {
                    Toast.makeText(AddListActivity.this, "Ocorreu um erro ao criar a lista",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ShoppingList> call, Throwable t) {
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
