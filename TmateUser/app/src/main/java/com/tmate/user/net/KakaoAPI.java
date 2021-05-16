package com.tmate.user.net;

import com.tmate.user.data.PaymentRes;
import com.tmate.user.data.SubscriptionRes;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface KakaoAPI {
    @FormUrlEncoded
    @POST("v1/payment/ready")   // 결제 준비 API
    Call<PaymentRes> kakaoReady(@Header("Authorization") String token, @FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("v1/payment/subscription")
    Call<SubscriptionRes> kakaoSubscription(@Header("Authorization") String token, @FieldMap Map<String, String> data);
}
