package com.org.pawpal;

import android.support.multidex.MultiDexApplication;

import com.org.pawpal.Utils.Constants;
import com.org.pawpal.server.ApiInterceptor;
import com.org.pawpal.server.PawPalAPI;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hp-pc on 24-11-2016.
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication sInstance;
    private static Retrofit retrofit;
    private static PawPalAPI pawPalAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initRetrofit();
    }

    private void initRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ApiInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pawPalAPI = retrofit.create(PawPalAPI.class);
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public PawPalAPI getPawPalAPI() {
        return pawPalAPI;

    }
}
