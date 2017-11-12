package com.example.william.shopplist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.william.shopplist.model.MetaItem;
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
public class AddListActivity extends AppCompatActivity {
    static ArrayAdapter metaItemAdapter;
    static ServerInterface servidor;
    static ListView metaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        servidor = ServerConnection.getInstance().getServidor();

        metaItemAdapter = new ArrayAdapter<MetaItem>(this, android.R.layout.simple_list_item_1,new ArrayList<MetaItem>());
        setContentView(R.layout.addlist_activity);

        metaItems = (ListView) findViewById(R.id.addlist_metaitems);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.addlist_toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Adicionar lista");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fillMetaItemList();
    }

    public static void fillMetaItemList(){
        Call<List<MetaItem>> retorno = servidor.getAllMetaItems();

        Log.i("DSI2017","Chamando servidor");

        retorno.enqueue(new Callback<List<MetaItem>>() {
            @Override
            public void onResponse(Call<List<MetaItem>> call, Response<List<MetaItem>> response) {
                List<MetaItem> listData = response.body();

                if(listData != null) {
                    metaItemAdapter.clear();
                    metaItemAdapter.addAll(listData);
                }
            }

            @Override
            public void onFailure(Call<List<MetaItem>> call, Throwable t) {
                Log.i("DSI2017", "Não deu");
            }
        });
        metaItems.setAdapter(metaItemAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
