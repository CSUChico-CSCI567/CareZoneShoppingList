package com.coding.doublea.carezoneshoppinglist.api;

import com.coding.doublea.carezoneshoppinglist.models.ShoppingItem;
import com.squareup.okhttp.Call;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * Created by Aaron on 4/4/2015.
 */
public interface ShoppingListService {

    /**
     * Fetch shopping list for user
     * @param cb
     */
    @Headers("Accept: application/json")
    @GET("/items") void fetchItems(
            CZRetrofitCallback<ArrayList<ShoppingItem>> cb
    );

}
