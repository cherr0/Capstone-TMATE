package com.tmate.user.net;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataService {



    /*
    *   서버 연결
    * */

    // EC2 서버 URL
     public static final String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/"; // 기본 URL

    // 안드로이드 폰 연결
//    public static final String BASE_URL = "http://192.168.1.25:8080";


    private static DataService instance;

    public MemberAPI memberAPI;
    public CommonAPI commonAPI;
    public MatchAPI matchAPI;
    public PaymentAPI paymentAPI;

    private DataService() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        memberAPI = retrofitClient.create(MemberAPI.class);
        commonAPI = retrofitClient.create(CommonAPI.class);
        matchAPI = retrofitClient.create(MatchAPI.class);
        paymentAPI = retrofitClient.create(PaymentAPI.class);

    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }


}
