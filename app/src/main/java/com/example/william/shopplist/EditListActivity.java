package com.example.william.shopplist;

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
public class EditListActivity extends AppCompatActivity {
    static MetaItemAdapter metaItemAdapter;
    static ServerInterface server;
    static ListView metaItems;
    static ShoppingList list;
    static CheckBox markAll;
    static ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        list = (ShoppingList) i.getSerializableExtra("list");

        server = ServerConnection.getInstance().getServer();

        metaItemAdapter = new MetaItemAdapter(this, R.layout.lists_adapter, new ArrayList<MetaItemList>());
        setContentView(R.layout.edit_list);

        metaItems = (ListView) findViewById(R.id.editlist_metaitems);

        spinner = (ProgressBar)findViewById(R.id.progress);
        spinner.setVisibility(View.VISIBLE);
        markAll = (CheckBox) findViewById(R.id.check_all);
        markAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillMetaItemList(markAll.isChecked());
            }
        });

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Adicionar lista");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        EditText description = (EditText) findViewById(R.id.editText);
        description.setText(list.getDescription());


        fillMetaItemList(markAll.isChecked());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_createList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText description = (EditText) findViewById(R.id.editText);
                list.setDescription(description.getText().toString());

                if (description.getText().toString().compareTo("") != 0) {
                    list.removeAllItems();

                    for(int i=0; i < metaItemAdapter.getCount(); i++) {
                        if (metaItemAdapter.getItem(i).isChecked()) {
                            ListItem listItem = new ListItem();
                            listItem.setMetaItem(metaItemAdapter.getItem(i).getMetaItem());
                            if (metaItemAdapter.getItem(i).getMetaItem() != null) {
                                Log.i("DSI2017 listitem", listItem.getDescription());
                                list.addListItem(listItem);
                            }
                        }
                    }

                    updateShoppingList(list);
                } else {
                    Toast.makeText(EditListActivity.this, "Informe um nome para esta lista",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void fillMetaItemList(boolean check){
        Call<List<MetaItem>> request = server.getAllMetaItems(list.getUserId());
        final boolean checkItems = check;

        request.enqueue(new Callback<List<MetaItem>>() {
            @Override
            public void onResponse(Call<List<MetaItem>> call, Response<List<MetaItem>> response) {
                List<MetaItem> listData = response.body();
                spinner.setVisibility(View.GONE);

                if(listData != null) {

                    metaItemAdapter.clear();

                    for(int i=0; i < listData.size(); i++) {

                        MetaItemList mi = new MetaItemList();
                        mi.setMetaItem(listData.get(i));
                        boolean checked = false;

                        for(int j=0; j < list.getItems().size(); j++) {
                            MetaItem item = list.getItems().get(j).getMetaItem();
                            if (item.equals(listData.get(i))) {
                                checked = true;
                            }
                        }

                        if (checked) {
                            mi.setChecked();
                        } else {
                            mi.unsetChecked();
                        }

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

    public void updateShoppingList(ShoppingList list) {
        Call<Void> request = server.updateShoppingList(list.getId(), list);


        request.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                onBackPressed();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditListActivity.this, "Ocorreu um erro ao editar a lista",
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
