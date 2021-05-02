package com.tmate.user.net;



import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataService {



    /*
    *   서버 연결
    * */

    // EC2 서버 URL
    // private static final String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/common/"; // 기본 URL

    // 안드로이드 폰 연결
//    private static final String BASE_URL = "http://172.26.2.39:9090/";

    private static final String BASE_URL = "http://172.30.1.59:9090/";

    private static DataService instance;

    public MemberAPI memberAPI;
    public CommonAPI commonAPI;
    public MatchAPI matchAPI;

    private DataService() {
        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        memberAPI = retrofitClient.create(MemberAPI.class);
        commonAPI = retrofitClient.create(CommonAPI.class);
        matchAPI = retrofitClient.create(MatchAPI.class);


    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }


}
