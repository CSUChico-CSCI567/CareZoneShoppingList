package com.coding.doublea.carezoneshoppinglist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.coding.doublea.carezoneshoppinglist.adapters.CZListAdapter;
import com.coding.doublea.carezoneshoppinglist.api.APIHelper;
import com.coding.doublea.carezoneshoppinglist.api.CZRetrofitCallback;
import com.coding.doublea.carezoneshoppinglist.api.ShoppingListService;
import com.coding.doublea.carezoneshoppinglist.models.ShoppingItem;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

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
        fab.setOnClickListener(fabClickListener);
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
            addItemsToList(data);
        } else {
            fetchData();
        }
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Button callbacks for the material dialog button
            MaterialDialog.ButtonCallback materialCallbacks = new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    View v = dialog.getCustomView();
                    if (v != null) {
                        EditText categoryText = (EditText) dialog.getCustomView().findViewById(R.id.add_category);
                        EditText nameText = (EditText) dialog.getCustomView().findViewById(R.id.add_name);
                        String category = String.valueOf(categoryText.getText());
                        String name = String.valueOf(nameText.getText());
                        postData(category, name);

                    }
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    dialog.cancel();
                }
            };
            //create a new material dialog and show it
            new MaterialDialog.Builder(MainActivity.this)
                    .title(R.string.dialog_add_title)
                    .titleColor(R.color.white)
                    .customView(R.layout.add_edit_dialog, true)
                    .positiveText(R.string.dialog_add_item_button)
                    .negativeText(R.string.dialog_cancel_button)
                    .callback(materialCallbacks)
                    .show();
        }
    };


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_DATA_KEY, mListAdapter.getData());
    }

    private RecyclerView.Adapter getAdapter() {
        if (mListAdapter == null) {
            mListAdapter = new CZListAdapter(this);
            mListAdapter.setData(new ArrayList<ShoppingItem>());
            mListAdapter.setOnItemClickListener(
                    new CZListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(final ShoppingItem item) {

                            MaterialDialog.ButtonCallback materialCallbacks = new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    View v = dialog.getCustomView();
                                    if (v != null) {
                                        EditText categoryText = (EditText) dialog.getCustomView().findViewById(R.id.add_category);
                                        EditText nameText = (EditText) dialog.getCustomView().findViewById(R.id.add_name);
                                        item.setCategory(String.valueOf(categoryText.getText()));
                                        item.setName(String.valueOf(nameText.getText()));
                                        updateData(item);
                                    }
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                    deleteData(item);
                                }

                                @Override
                                public void onNeutral(MaterialDialog dialog) {
                                    super.onNeutral(dialog);
                                    dialog.cancel();
                                }
                            };
                            final MaterialDialog updateDialog = new MaterialDialog.Builder(MainActivity.this)
                                    .title(getString(R.string.dialog_update_title))
                                    .titleColor(R.color.white)
                                    .customView(R.layout.add_edit_dialog, true)
                                    .positiveText(getString(R.string.dialog_update_button))
                                    .negativeText(getString(R.string.dialog_delete_button))
                                    .neutralText(R.string.dialog_cancel_button)
                                    .callback(materialCallbacks)
                                    .build();

                            DialogInterface.OnShowListener onShowListener = new DialogInterface.OnShowListener() {

                                @Override
                                public void onShow(DialogInterface dialog) {
                                    if (updateDialog != null) {
                                        View v = updateDialog.getCustomView();
                                        if (v != null) {
                                            EditText categoryText = (EditText) v.findViewById(R.id.add_category);
                                            EditText nameText = (EditText) v.findViewById(R.id.add_name);
                                            categoryText.setText(item.getCategory());
                                            nameText.setText(item.getName());
                                        }
                                    }
                                }
                            };
                            updateDialog.setOnShowListener(onShowListener);
                            updateDialog.show();
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
                        addItemsToList(shoppingItems);
                    }
                }
        );
    }

    private void postData(String category, String name) {
        APIHelper api = APIHelper.getInstance(this);
        final ShoppingItem item = new ShoppingItem(category, name);
        api.createAPIInterface(ShoppingListService.class).postItem(
                item,
                new CZRetrofitCallback<ShoppingItem>() {
                    @Override
                    public void success(ShoppingItem shoppingItem, Response response) {
                        super.success(shoppingItem, response);
                        addItemToList(shoppingItem);
                    }
                }
        );
    }

    private void updateData(ShoppingItem item) {
        APIHelper api = APIHelper.getInstance(this);
        api.createAPIInterface(ShoppingListService.class).putItem(
                item,
                item.getId(),
                new CZRetrofitCallback<ShoppingItem>() {
                    @Override
                    public void success(ShoppingItem shoppingItem, Response response) {
                        super.success(shoppingItem, response);
                        updateList();
                    }
                }
        );
    }
    private void deleteData(final ShoppingItem item) {
        APIHelper api = APIHelper.getInstance(this);
        api.createAPIInterface(ShoppingListService.class).removeItem(
                item.getId(),
                new CZRetrofitCallback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        super.success(response, response2);
                        mListAdapter.removeData(item);
                        updateList();
                    }
                }
        );
    }

    private void addItemsToList(ArrayList<ShoppingItem> data) {
        if (data != null) {
            if (!data.isEmpty()) {
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

    private void addItemToList(ShoppingItem item) {
        if (item != null) {
            mListAdapter.addData(item);
            updateList();
        }
    }

    private void updateList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListAdapter.notifyDataSetChanged();
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
        }

        return super.onOptionsItemSelected(item);
    }
}
