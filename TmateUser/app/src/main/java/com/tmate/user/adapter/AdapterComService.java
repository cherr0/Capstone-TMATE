package com.tmate.user.adapter;

import com.tmate.user.data.EventDTO;
import com.tmate.user.data.Notice;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class AdapterComService {

    /* ------------------------
             서버 연결
      ------------------------ */
    private String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/common/"; // 기본 URL
//    private String BASE_URL = "http://10.0.2.2:9090/common/";

    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    NoticeAPI notice = retrofitClient.create(NoticeAPI.class);
    EventAPI event = retrofitClient.create(EventAPI.class);
    DeleteAPI delete = retrofitClient.create(DeleteAPI.class);
}

interface NoticeAPI {
    // 공지 세부 내용 조회
    @GET("notice/{bd_id}")
    Call<Notice> getNoticeDetail(@Path("bd_id") String bd_id);
}

interface EventAPI {
    @GET("readevent/{bd_id}")
    Call<EventDTO> readEvent(@Path("bd_id") String bd_id);
}

interface DeleteAPI {
    @DELETE("deletebookmark/{bm_id}/{m_id}")
    Call<Boolean> removeBookmark(@Path("bm_id") String bm_id, @Path("m_id") String m_id);
}
