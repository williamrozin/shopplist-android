package com.example.william.shopplist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by william on 11/11/17.
 */
public class AddListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlist_activity);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.addlist_toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Adicionar lista");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
