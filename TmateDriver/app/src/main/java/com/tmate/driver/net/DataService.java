package com.tmate.driver.net;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataService {

    /* ------------------------
             서버 연결
      ------------------------ */

//    private static final String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080"; // 기본 URL
    //private static final String BASE_URL = "http://192.168.1.21:9090"; // 로컬 URL

    // 안드로이드 폰 연결
    private static final String BASE_URL = "http://192.168.0.141:9090";

//    private static final String BASE_URL = "http://10.0.2.2:9090";
    private static DataService instance;
    public DriverAPI driver;
    public CommonAPI common;
    public CallAPI call;
    private DataService() {
        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        driver = retrofitClient.create(DriverAPI.class);
        common = retrofitClient.create(CommonAPI.class);
        call = retrofitClient.create(CallAPI.class);
    }

    public static DataService getInstance() {
        if(instance==null) {
            instance = new DataService();
        }
        return instance;
    }

    

}
