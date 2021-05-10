package com.tmate.user.net;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentAPI {

    @POST("api/kakaoReady")
    Call<Boolean> kakaoReady(@Body Map<String, String> map);
}
