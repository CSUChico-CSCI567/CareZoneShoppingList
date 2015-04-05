package com.coding.doublea.carezoneshoppinglist.api;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.util.Log;

import com.coding.doublea.carezoneshoppinglist.R;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Aaron on 4/4/2015.
 */
public class APIHelper {
    private static final String TAG = APIHelper.class.getSimpleName();

    private final OkHttpClient mClient;
    private static APIHelper mInstance;
    private Context mContext;


    public APIHelper(Context applicationContext) {
        mContext = applicationContext;
        File cacheDir = new File(mContext.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache httpResponseCache = null;
        httpResponseCache = new Cache(cacheDir, cacheSize);


        Cache cache = new Cache(cacheDir, cacheSize);

        mClient = new OkHttpClient();
        mClient.setCache(cache);
    }

    public static synchronized APIHelper getInstance(Context context){
        if(mInstance == null) {
            mInstance = new APIHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * create the rest adapter for retrofit
     */
    public <S> S createAPIInterface(Class<S> serviceClass) {
        Executor executor = Executors.newCachedThreadPool();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setExecutors(executor, executor)
                .setEndpoint(mContext.getString(R.string.cz_url))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(mClient))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("X-CZ-Authorization", mContext.getString(R.string.cz_api_key));
                    }
                })
                .build();
        return restAdapter.create(serviceClass);
    }
}
