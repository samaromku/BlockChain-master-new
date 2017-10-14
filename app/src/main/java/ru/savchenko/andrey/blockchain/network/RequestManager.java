package ru.savchenko.andrey.blockchain.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static ru.savchenko.andrey.blockchain.storage.Const.BASE_URL;

/**
 * Created by Andrey on 12.09.2017.
 */

public class RequestManager {
    private static Retrofit retrofit;

    public static void init() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    private static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static RetrofitService getRetrofitService() {
        return createService(RetrofitService.class);
    }

}
