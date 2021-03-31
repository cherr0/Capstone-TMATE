package com.tmate.user.Fragment;

import com.tmate.user.data.Member;
import com.tmate.user.data.PhoneDTO;
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
    // 애뮬레이터용
//    private String BASE_URL = "http://10.0.2.2:9090/member/";
    private String BASE_URL = "http://172.26.2.70:9090/member/"; // 기본 URL

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

    // 휴대폰 인증
    @POST("sendsms")
    Call<Boolean> sendSMS(@Body PhoneDTO phoneDTO);

    // 휴대폰 인증 정보 확인
    @POST("confirm")
    Call<Integer> confirm(@Body PhoneDTO phoneDTO);
}
