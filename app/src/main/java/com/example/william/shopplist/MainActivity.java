package com.example.william.shopplist;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ListView;

import com.example.william.shopplist.activities.categories.AddCategoryActivity;
import com.example.william.shopplist.activities.metaitems.AddMetaItemActivity;
import com.example.william.shopplist.adapter.CategoriesAdapter;
import com.example.william.shopplist.adapter.ListsAdapter;
import com.example.william.shopplist.adapter.MetaItemsAdapter;
import com.example.william.shopplist.activities.lists.AddListActivity;
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
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int tab;
    private ViewPager mViewPager;

    static ServerInterface server;
    static SwipeRefreshLayout listsSwipeRefresh;
    static SwipeRefreshLayout metaItemsSwipeRefresh;
    static SwipeRefreshLayout categoriesSwipeRefresh;
    static ListsAdapter listsAdapter;
    static CategoriesAdapter categoriesAdapter;
    static MetaItemsAdapter metaItemAdapter;
    static ListView lists;
    static ListView metaItems;
    static ListView categories;
    static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        server = ServerConnection.getInstance().getServer();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (user.getName() != null) {
            toolbar.setTitle("Olá, " + user.getName() + "!");
        }

        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mViewPager.getCurrentItem()){
                    case 1:
                        Intent addMetaItem = new Intent(MainActivity.this, AddMetaItemActivity.class);
                        addMetaItem.putExtra("user", user);
                        startActivity(addMetaItem);
                        return;
                    case 2:
                        Intent addCategory = new Intent(MainActivity.this, AddCategoryActivity.class);
                        addCategory.putExtra("user", user);
                        startActivity(addCategory);
                        return;
                    default:
                        Intent addList = new Intent(MainActivity.this, AddListActivity.class);
                        addList.putExtra("user", user);
                        startActivity(addList);
                        break;
                }
            }
        });

        listsAdapter = new ListsAdapter(this, R.layout.lists_adapter, new ArrayList<ShoppingList>());
        metaItemAdapter = new MetaItemsAdapter(this, android.R.layout.simple_list_item_1,new ArrayList<MetaItem>());
        categoriesAdapter = new CategoriesAdapter(this, android.R.layout.simple_list_item_1,new ArrayList<Category>());
    }

    @Override
    public void onRestart() {
        super.onRestart();
        switch (mViewPager.getCurrentItem()){
            case 1:
                updateItems();
                break;
            case 2:
                updateCategories();
                break;
            default:
                updateLists();
                break;
        }
    }

    public static void updateLists(){
        Call<List<ShoppingList>> request = server.getAllShoppingLists(user.getId());

        request.enqueue(new Callback<List<ShoppingList>>() {
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
                listsSwipeRefresh.setRefreshing(false);
            }
        });

    }


    public static void updateItems(){
        Call<List<MetaItem>> request = server.getAllMetaItems(user.getId());

        request.enqueue(new Callback<List<MetaItem>>() {
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
                metaItemsSwipeRefresh.setRefreshing(false);
            }
        });

    }


    public static void updateCategories(){
        Call<List<Category>> request = server.getAllCategories(user.getId());

        request.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> listData = response.body();

                if(listData != null) {
                    categoriesAdapter.clear();
                    categoriesAdapter.addAll(listData);
                    categoriesSwipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                categoriesSwipeRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
            listsSwipeRefresh.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            listsSwipeRefresh.setRefreshing(true);
                            updateLists();
                        }
                    }
            );
            listsSwipeRefresh.setRefreshing(true);
            updateLists();
            return rootView;
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
                            metaItemsSwipeRefresh.setRefreshing(true);
                            updateItems();
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
            categoriesSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_categories);
            categories.setAdapter(categoriesAdapter);
            categoriesSwipeRefresh.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            categoriesSwipeRefresh.setRefreshing(true);
                            updateCategories();
                        }
                    }
            );

            updateCategories();
            return rootView;
        }
    }

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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Minhas Listas";
                case 1:
                    return "Produtos";
                case 2:
                    return "Categorias";
            }
            return null;
        }
    }
}

