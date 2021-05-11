package com.tmate.user.net;

import com.tmate.user.data.PaymentRes;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface KakaoAPI {
    @FormUrlEncoded
    @POST("v1/payment/ready")
    Call<PaymentRes> kakaoReady(@Header("Authorization") String token, @FieldMap HashMap<String, String> data);
}
