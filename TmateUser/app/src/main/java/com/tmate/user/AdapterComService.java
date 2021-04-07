package com.tmate.user;



import com.tmate.user.data.EventDTO;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class AdapterComService {

    private String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/common/";

    Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    EventReadAPI eventReadAPI = retrofitClient.create(EventReadAPI.class);

}

interface EventReadAPI {

    @GET("readevent/{bd_id}")
    Call<EventDTO> readEvent(@Path("bd_id") String bd_id);
}