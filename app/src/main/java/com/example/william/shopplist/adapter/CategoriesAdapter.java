package com.example.william.shopplist.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.william.shopplist.R;
import com.example.william.shopplist.activities.categories.SelectedCategoryActivity;
import com.example.william.shopplist.activities.lists.SelectedListActivity;
import com.example.william.shopplist.model.Category;

import java.io.Serializable;
import java.util.List;

/**
 * Created by william on 15/11/17.
 */
public class CategoriesAdapter extends ArrayAdapter<Category> implements Serializable {

    public CategoriesAdapter(Context context, int resource, List<Category> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final Category category;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.lists_adapter, null);
        }

        category = getItem(position);

        if (category != null) {
            TextView tt3 = (TextView) v.findViewById(R.id.description);
            TextView tt2 = (TextView) v.findViewById(R.id.date);

            if (tt2 != null) {
                tt2.setText("Cód. " + Long.toString(category.getId()));
            }

            if (tt3 != null) {
                tt3.setText(category.getDescription());
            }

        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openItem = new Intent(view.getContext(), SelectedCategoryActivity.class);
                openItem.putExtra("category", category);
                view.getContext().startActivity(openItem);
            }
        });
        return v;
    }

}