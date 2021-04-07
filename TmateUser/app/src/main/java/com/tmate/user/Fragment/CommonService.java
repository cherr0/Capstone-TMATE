package com.tmate.user.Fragment;

import com.tmate.user.data.Notice;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class CommonService {
    /* ------------------------
              서버 연결
       ------------------------ */
    private String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/common/"; // 기본 URL

    Retrofit retrofitClient =
            new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    public NoticeAPI notice = retrofitClient.create(NoticeAPI.class);
}

interface NoticeAPI {
    // 공지사항 리스트 조회
    @GET("noticeList")
    Call<List<Notice>> getNoticeList();
}
