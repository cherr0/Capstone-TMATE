package com.tmate.user.net;

import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KakaoService {
    // Kakao URL
    private static final String BASE_URL = "https://kapi.kakao.com/";
    private final KakaoAPI api;

    private static KakaoService instance;

    private KakaoService() {
        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateFormatAdapter());

        Retrofit retrofitClient =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(new OkHttpClient.Builder().build())
                        .addConverterFactory(GsonConverterFactory.create(builder.create()))
                        .build();
        api = retrofitClient.create(KakaoAPI.class);
    }

    public static KakaoService getInstance() {
        if(instance == null) instance = new KakaoService();
        return instance;
    }

    public KakaoAPI getApi() {
        return api;
    }
}
