package com.tmate.user.net;

import com.tmate.user.data.History;
import com.tmate.user.data.Together;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MatchAPI {

    @GET("match/getlist/{slttd}/{slngtd}/{flttd}/{flngtd}")
    Call<List<History>> getMatchingList(@Path("slttd") String slttd, @Path("slngtd") String slngtd, @Path("flttd") String flttd, @Path("flngtd") String flngtd);

    @GET("match/read/{merchant_uid}")
    Call<History> getMatchingDetail(@Path("merchant_uid") String merchant_uid);

//    @POST("match/register/matching")
//    Call<Boolean> registerMatchingRegister(@Body History history, @Body Together together);

    @POST("match/register/matching")
    Call<Boolean> registerMatchingRegister(@Body HashMap<String,Object> hashMap);
}
