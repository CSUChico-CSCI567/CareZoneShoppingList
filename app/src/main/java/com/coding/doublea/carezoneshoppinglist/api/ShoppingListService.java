package com.coding.doublea.carezoneshoppinglist.api;

import com.coding.doublea.carezoneshoppinglist.models.ShoppingItem;

import java.util.ArrayList;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Aaron on 4/4/2015.
 */
public interface ShoppingListService {

    /**
     * Fetch shopping list for user
     *
     * @param cb
     */
    @Headers("Accept: application/json")
    @GET("/items")
    void fetchItems(
            CZRetrofitCallback<ArrayList<ShoppingItem>> cb
    );

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"})
    @POST("/items")
    void postItem(
            @Body ShoppingItem shoppingItem,
            CZRetrofitCallback<ShoppingItem> cb
    );

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"})
    @PUT("/items/{id}")
    void putItem(
            @Body ShoppingItem shoppingItem,
            @Path("id") int id,
            CZRetrofitCallback<ShoppingItem> cb
    );

    @DELETE("/items/{id}")
    void removeItem(
            @Path("id") int id,
            CZRetrofitCallback<Response> cb
    );





}
