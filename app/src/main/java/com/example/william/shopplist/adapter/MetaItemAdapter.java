package com.example.william.shopplist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.william.shopplist.R;
import com.example.william.shopplist.model.MetaItemList;

import java.io.Serializable;
import java.util.List;

/**
 * Created by william on 15/11/17.
 */
public class MetaItemAdapter extends ArrayAdapter<MetaItemList> implements Serializable {

    public MetaItemAdapter(Context context, int resource, List<MetaItemList> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final MetaItemList metaItemList;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.check_adapter, null);
        }

        metaItemList = getItem(position);

        if (metaItemList != null) {
            CheckBox cb = (CheckBox) v.findViewById(R.id.check_item);
            TextView tv = (TextView) v.findViewById(R.id.category);

            tv.setText(metaItemList.getMetaItem().getCategory().getDescription());
            tv.setTextColor(Color.parseColor(metaItemList.getMetaItem().getCategory().getColor()));

            if (cb!= null) {
                cb.setText(metaItemList.getMetaItem().getDescription());
                cb.setChecked(metaItemList.isChecked());
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        metaItemList.toggleChecked();
                    }
                });
            }

        }
        return v;
    }
}