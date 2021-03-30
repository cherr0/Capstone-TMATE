package com.tmate.user.Fragment;

import com.tmate.user.data.Member;
import com.tmate.user.data.UserHistroy;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class DataService {

    private String BASE_URL = "http://172.26.1.156:9090/member/"; // 기본 URL

    Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public InsertAPI insert = retrofitClient.create(InsertAPI.class);

    public SelectAPI select = retrofitClient.create(SelectAPI.class);

}


interface InsertAPI {
    @POST("register")
    Call<Boolean> insertOne(@Body Map<String, String> map);
}

interface SelectAPI {
    // 상세 프로필 정보
    @GET("select/{m_id}")
    Call<Member> selectProfile(@Path("m_id") String m_id);

    // 유저 이용 내역 정보
    @GET("historys/{m_id}")
    Call<List<UserHistroy>> selectHistory(@Path("m_id") String m_id);
}
