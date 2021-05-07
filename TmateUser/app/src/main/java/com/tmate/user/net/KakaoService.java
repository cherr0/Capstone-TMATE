package com.tmate.user.net;

import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KakaoService {
    private static final String BASE_URL = "https://kapi.kakao.com";

    GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateFormatAdapter());

    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create(builder.create()))
                    .build();

    KakaoAPI kakao = retrofitClient.create(KakaoAPI.class);
}
