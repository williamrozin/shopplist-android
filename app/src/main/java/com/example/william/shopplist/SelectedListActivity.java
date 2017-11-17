package com.example.william.shopplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.william.shopplist.model.ListItem;
import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by william on 11/11/17.
 */
public class SelectedListActivity extends AppCompatActivity implements Serializable{
    static ArrayAdapter<ListItem> adapter;
    static ServerInterface servidor;
    static ListView listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        ShoppingList list = (ShoppingList) i.getSerializableExtra("list");
        servidor = ServerConnection.getInstance().getServidor();

        adapter = new ArrayAdapter<ListItem>(this, android.R.layout.simple_list_item_1,new ArrayList<ListItem>());
        setContentView(R.layout.selected_list);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(list.getDescription());

        Log.i("DSI2017", list.getDescription());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        adapter.addAll(list.getItems());

        listItems = (ListView) findViewById(R.id.list);
        listItems.setAdapter(adapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
