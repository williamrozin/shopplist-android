package com.example.william.shopplist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.william.shopplist.R;
import com.example.william.shopplist.model.MetaItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by william on 15/11/17.
 */
public class MetaItemsAdapter extends ArrayAdapter<MetaItem> implements Serializable {

    public MetaItemsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MetaItemsAdapter(Context context, int resource, List<MetaItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final MetaItem metaItem;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.lists_adapter, null);
        }

        metaItem = getItem(position);

        if (metaItem != null) {
            TextView tt2 = (TextView) v.findViewById(R.id.date);
            TextView tt3 = (TextView) v.findViewById(R.id.description);

            if (tt2 != null) {
                tt2.setText(metaItem.getCategory().getDescription());
                tt2.setTextColor(Color.parseColor(metaItem.getCategory().getColor()));
            }

            if (tt3 != null) {
                tt3.setText(metaItem.getDescription());
            }

        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent openItem = new Intent(view.getContext(), SelectedListActivity.class);
                //view.getContext().startActivity(openItem);
            }
        });
        return v;
    }

}