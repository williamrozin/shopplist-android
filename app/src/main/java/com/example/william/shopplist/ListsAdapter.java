package com.example.william.shopplist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.william.shopplist.model.ShoppingList;

import java.io.Serializable;
import java.util.List;

/**
 * Created by william on 15/11/17.
 */
public class ListsAdapter extends ArrayAdapter<ShoppingList> implements Serializable {

    public ListsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListsAdapter(Context context, int resource, List<ShoppingList> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ShoppingList shopplist;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.lists_adapter, null);

            if (position % 2 == 0) {
                v.setBackgroundColor(Color.parseColor("#F5F5F5"));
            }
        }

        shopplist = getItem(position);

        if (shopplist != null) {
            TextView tt2 = (TextView) v.findViewById(R.id.date);
            TextView tt3 = (TextView) v.findViewById(R.id.description);

            if (tt3 != null) {
                tt3.setText(shopplist.getDescription());
            }

            if (tt2 != null) {
                tt2.setText(shopplist.getDate());
            }
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openItem = new Intent(view.getContext(), SelectedList.class);
                openItem.putExtra("list", shopplist);
                view.getContext().startActivity(openItem);
            }
        });
        return v;
    }

}