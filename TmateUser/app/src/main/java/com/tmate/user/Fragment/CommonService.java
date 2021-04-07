package com.tmate.user.Fragment;

import com.tmate.user.data.EventDTO;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CommonService {

    private String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/common/";

    Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    EventAPI eventAPI = retrofitClient.create(EventAPI.class);


}

interface EventAPI{

    // 진행중인 이벤트 리스트 던지는 것
    @GET("eventplist")
    Call<List<EventDTO>> getEventList();

    // 종료된 이벤트 리스트 던지는 것
    @GET("eventflist")
    Call<List<EventDTO>> getFinishEventList();

    // 글 상세보기
    @GET("readevent/{bd_id}")
    Call<EventDTO> readEvent(@Path("bd_id") String bd_id);

}
