package com.example.william.shopplist.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
    static ServerInterface server;
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
        server = ServerConnection.getInstance().getServer();

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.check_adapter, null);
        }

        listItem = getItem(position);

        if (listItem != null) {
            final CheckBox cb = (CheckBox) v.findViewById(R.id.check_item);
            TextView tv = (TextView) v.findViewById(R.id.category);

            tv.setText(listItem.getMetaItem().getCategory().getDescription());
            tv.setTextColor(Color.parseColor(listItem.getMetaItem().getCategory().getColor()));
            if (cb!= null) {
                MetaItem metaItem = listItem.getMetaItem();
                cb.setText(metaItem.getDescription());
                cb.setChecked(listItem.isChecked());
                if (listItem.isChecked()) {
                    cb.setPaintFlags(cb.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    cb.setPaintFlags(cb.getPaintFlags() & ( ~ Paint.STRIKE_THRU_TEXT_FLAG));
                }

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listItem.isChecked()) {
                            listItem.unsetChecked();
                            unsetAsChecked(listItem.getId());
                            cb.setPaintFlags(cb.getPaintFlags() & ( ~ Paint.STRIKE_THRU_TEXT_FLAG));
                        } else {
                            listItem.setChecked();
                            cb.setPaintFlags(cb.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            setAsChecked(listItem.getId());
                        }
                    }
                });
            }

        }
        return v;
    }

    public void setAsChecked(long id) {
        Call<Void> request = server.checkItem(id);

        request.enqueue(new Callback<Void>() {
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
        Call<Void> request = server.uncheckItem(id);

        request.enqueue(new Callback<Void>() {
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