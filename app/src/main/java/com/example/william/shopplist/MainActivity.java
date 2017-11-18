package com.example.william.shopplist;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.william.shopplist.adapter.CategoriesAdapter;
import com.example.william.shopplist.adapter.ListsAdapter;
import com.example.william.shopplist.adapter.MetaItemsAdapter;
import com.example.william.shopplist.model.Category;
import com.example.william.shopplist.model.MetaItem;
import com.example.william.shopplist.model.ShoppingList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.william.shopplist.model.User;
import com.example.william.shopplist.server.ServerConnection;
import com.example.william.shopplist.server.ServerInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Serializable{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    static ServerInterface servidor;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    static SwipeRefreshLayout listsSwipeRefresh;
    static SwipeRefreshLayout metaItemsSwipeRefresh;
    static SwipeRefreshLayout categorieswipeRefresh;
    static ListsAdapter listsAdapter;
    static CategoriesAdapter categoriesAdapter;
    static MetaItemsAdapter metaItemAdapter;
    static ListView lists;
    static ListView metaItems;
    static ListView categories;
    static User user;
    public int tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        servidor = ServerConnection.getInstance().getServidor();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (user.getName() != null) {
            toolbar.setTitle("Olá, " + user.getName() + "!");
        }


        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DSI2017", Integer.toString(mViewPager.getCurrentItem()));
                //view.getWindowVisibility();
                switch (mViewPager.getCurrentItem()){
                    case 1:
                        return;
                    case 2:
                        return;
                    default:
                        Intent addItem = new Intent(MainActivity.this, AddListActivity.class);
                        addItem.putExtra("user", user);
                        startActivity(addItem);
                        break;
                }
            }
        });

        listsAdapter = new ListsAdapter(this, R.layout.lists_adapter, new ArrayList<ShoppingList>());
        metaItemAdapter = new MetaItemsAdapter(this, android.R.layout.simple_list_item_1,new ArrayList<MetaItem>());
        categoriesAdapter = new CategoriesAdapter(this, android.R.layout.simple_list_item_1,new ArrayList<Category>());
    }

    public static void updateLists(){
        Call<List<ShoppingList>> retorno = servidor.getAllShoppingLists(user.getId());

        Log.i("DSI2017","Chamando servidor");

        retorno.enqueue(new Callback<List<ShoppingList>>() {
            @Override
            public void onResponse(Call<List<ShoppingList>> call, Response<List<ShoppingList>> response) {
                List<ShoppingList> listData = response.body();

                if(listData != null) {
                    listsAdapter.clear();
                    listsAdapter.addAll(listData);
                    listsSwipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<ShoppingList>> call, Throwable t) {
                Log.i("DSI2017", "Não deu");
                listsSwipeRefresh.setRefreshing(false);
            }
        });

    }


    public static void updateItems(){
        Call<List<MetaItem>> retorno = servidor.getAllMetaItems(user.getId());

        Log.i("DSI2017","Chamando servidor");

        retorno.enqueue(new Callback<List<MetaItem>>() {
            @Override
            public void onResponse(Call<List<MetaItem>> call, Response<List<MetaItem>> response) {
                List<MetaItem> listData = response.body();

                if(listData != null) {
                    metaItemAdapter.clear();
                    metaItemAdapter.addAll(listData);
                    metaItemsSwipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<MetaItem>> call, Throwable t) {
                Log.i("DSI2017", "Não deu");
                metaItemsSwipeRefresh.setRefreshing(false);
            }
        });

    }


    public static void updateCategories(){
        Call<List<Category>> retorno = servidor.getAllCategories(user.getId());

        Log.i("DSI2017","Chamando servidor");

        retorno.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> listData = response.body();

                if(listData != null) {
                    categoriesAdapter.clear();
                    categoriesAdapter.addAll(listData);
                    categorieswipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.i("DSI2017", "Não deu");
                listsSwipeRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.logout) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ListsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.lists, container, false);
            lists = (ListView) rootView.findViewById(R.id.lists);
            listsSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_lists);
            lists.setAdapter(listsAdapter);
            Log.i("DSI2017", "AAAAAAAAAAAAAA");
            listsSwipeRefresh.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            listsSwipeRefresh.setRefreshing(true);
                            updateLists();
                        }
                    }
            );

            updateLists();
            return rootView;
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        switch (mViewPager.getCurrentItem()){
            case 1:
                updateItems();
                return;
            case 2:
                updateCategories();
                return;
            default:
                updateLists();
                break;
        }
    }

    public static class MetaItemsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.metaitems, container, false);

            metaItems = (ListView) rootView.findViewById(R.id.metaitems);
            metaItemsSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_metaitems);
            metaItems.setAdapter(metaItemAdapter);

            metaItemsSwipeRefresh.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            metaItemsSwipeRefresh.setRefreshing(false);
                        }
                    }
            );
            updateItems();
            return rootView;
        }
    }

    public static class CategoriesFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.categories, container, false);

            categories = (ListView) rootView.findViewById(R.id.categories);
            categorieswipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_categories);
            categories.setAdapter(categoriesAdapter);

            categorieswipeRefresh.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            categorieswipeRefresh.setRefreshing(false);
                        }
                    }
            );

            updateCategories();
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fm;
            switch (position) {
                case 1:
                    fm = new MetaItemsFragment();
                    break;
                case 2:
                    fm = new CategoriesFragment();
                    break;
                default:
                    fm = new ListsFragment();
                    break;
            }
            return fm;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Minhas Listas";
                case 1:
                    return "Itens";
                case 2:
                    return "Categorias";
            }
            return null;
        }
    }
}

