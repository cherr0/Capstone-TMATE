package com.tmate.user.Fragment;

import com.tmate.user.data.History;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MatchDataService {

        private String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/match/"; // 기본 URL
//    private String BASE_URL = "http://10.0.2.2:9090/match/";
    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


    MatchingApi matchingApi = retrofitClient.create(MatchingApi.class);
}

interface MatchingApi{

    @GET("getlist/{slttd}/{slngtd}/{flttd}/{flngtd}")
    Call<List<History>> getMatchingList(@Path("slttd") String slttd, @Path("slngtd") String slngtd, @Path("flttd") String flttd, @Path("flngtd") String flngtd);

    @POST("read")
    Call<History> getMatchingDetail(@Body String merchant_uid);

}
