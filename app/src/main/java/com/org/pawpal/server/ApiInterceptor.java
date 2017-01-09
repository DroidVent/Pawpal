package com.org.pawpal.server;

import com.org.pawpal.Utils.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hp-pc on 12-12-2016.
 */
public class ApiInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        request = request.newBuilder()
                .addHeader("appId", Constants.APP_ID)
                .addHeader("AppKey", Constants.APP_KEY) //new header added
                .build();

        request = request.newBuilder().build();


        Response response = chain.proceed(request);
        return response;
    }
}
