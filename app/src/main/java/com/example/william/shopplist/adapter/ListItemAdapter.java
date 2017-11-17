package com.example.william.shopplist.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.william.shopplist.R;
import com.example.william.shopplist.SelectedListActivity;
import com.example.william.shopplist.model.ListItem;
import com.example.william.shopplist.model.MetaItem;
import com.example.william.shopplist.model.MetaItemList;
import com.example.william.shopplist.model.ShoppingList;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by william on 15/11/17.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> implements Serializable {
    static ServerInterface servidor;
    public ListItemAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListItemAdapter(Context context, int resource, List<ListItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ListItem listItem;
        servidor = ServerConnection.getInstance().getServidor();

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.check_adapter, null);

            if (position % 2 == 0) {
                v.setBackgroundColor(Color.parseColor("#f5f5f5"));
            }
        }

        listItem = getItem(position);

        if (listItem != null) {
            CheckBox cb = (CheckBox) v.findViewById(R.id.check_item);

            if (cb!= null) {
                MetaItem metaItem = listItem.getMetaItem();
                cb.setText(metaItem.getDescription());
                cb.setChecked(listItem.isChecked());
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listItem.isChecked()) {
                            listItem.unsetChecked();
                            unsetAsChecked(listItem.getId());
                        } else {
                            listItem.setChecked();
                            setAsChecked(listItem.getId());
                        }
                    }
                });
            }

        }
        return v;
    }

    public void setAsChecked(long id) {
        Call<Void> retorno = servidor.checkItem(id);

        retorno.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("DSI2017", "item marcado");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("DSI2017", "erro ao marcar");
            }
        });
    }

    public void unsetAsChecked(long id) {
        Call<Void> retorno = servidor.uncheckItem(id);

        retorno.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("DSI2017", "item desmarcado");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("DSI2017", "erro ao desmarcar");
            }
        });
    }
}