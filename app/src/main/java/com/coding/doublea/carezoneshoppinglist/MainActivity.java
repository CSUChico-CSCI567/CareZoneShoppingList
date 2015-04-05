package com.coding.doublea.carezoneshoppinglist;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import com.coding.doublea.carezoneshoppinglist.adapters.CZListAdapter;
import com.coding.doublea.carezoneshoppinglist.api.APIHelper;
import com.coding.doublea.carezoneshoppinglist.api.CZRetrofitCallback;
import com.coding.doublea.carezoneshoppinglist.api.ShoppingListService;
import com.coding.doublea.carezoneshoppinglist.models.ShoppingItem;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SAVED_DATA_KEY = TAG + "shoppinglist";

    private RecyclerView mRecyclerView;
    private CZListAdapter mListAdapter = null;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(getAdapter());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                fetchData();
            }
        });

        if (savedInstanceState != null) {
            ArrayList<ShoppingItem> data;
            data = savedInstanceState.getParcelableArrayList(SAVED_DATA_KEY);
            updateList(data);
        } else {
            fetchData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_DATA_KEY, mListAdapter.getData());
    }

    private RecyclerView.Adapter getAdapter() {
        if(mListAdapter == null) {
            mListAdapter = new CZListAdapter(this);
            mListAdapter.setData(new ArrayList<ShoppingItem>());
            mListAdapter.setOnItemClickListener(
                    new CZListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(ShoppingItem item) {
                            Toast.makeText(MainActivity.this, "Shopping Item Name = " + item.getName(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }
        return mListAdapter;
    }

    private void fetchData() {
        APIHelper api = APIHelper.getInstance(this);
        api.createAPIInterface(ShoppingListService.class).fetchItems(
                new CZRetrofitCallback<ArrayList<ShoppingItem>>() {
                    @Override
                    public void success(ArrayList<ShoppingItem> shoppingItems, Response response) {
                        super.success(shoppingItems, response);
                        updateList(shoppingItems);
                    }
                }
        );
    }

    private void updateList(ArrayList<ShoppingItem> data) {
        if(data!=null) {
            if(!data.isEmpty()) {
                mListAdapter.setData(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        }
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
        }

        return super.onOptionsItemSelected(item);
    }
}
