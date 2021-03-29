package com.tmate.user.Fragment;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class DataService {

    private String BASE_URL = "http://172.26.1.156:9090/member/"; // 기본 URL

    Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public InsertAPI insert = retrofitClient.create(InsertAPI.class);
}


interface InsertAPI {
    @POST("register")
    Call<Boolean> insertOne(@Body Map<String, String> map);
}