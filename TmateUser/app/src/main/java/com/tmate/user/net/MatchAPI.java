package com.tmate.user.net;

import com.tmate.user.data.History;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MatchAPI {

    @GET("match/getlist/{slttd}/{slngtd}/{flttd}/{flngtd}")
    Call<List<History>> getMatchingList(@Path("slttd") String slttd, @Path("slngtd") String slngtd, @Path("flttd") String flttd, @Path("flngtd") String flngtd);

    @POST("match/read")
    Call<History> getMatchingDetail(@Body String merchant_uid);
}
