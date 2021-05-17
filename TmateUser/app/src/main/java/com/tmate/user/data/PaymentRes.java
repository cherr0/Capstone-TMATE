package com.tmate.user.data;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRes {

    // 결제 고유 번호
    @SerializedName("tid")
    private String tid;

    // 모바일 앱 결제 페이지
    @SerializedName("next_redirect_app_url")
    private String next_redirect_app_url;

    // 모바일 웹 결제 페이지
    @SerializedName("next_redirect_mobile_url")
    private String next_redirect_mobile_url;

    // PC 웹 결제 페이지
    @SerializedName("next_redirect_pc_url")
    private String next_redirect_pc_url;

    // 카카오페이 결제화면 이동 스키마 - Android
    @SerializedName("android_app_scheme")
    private String android_app_scheme;

    // 카카오페이 결제화면 이동 스키마 - iOS
    @SerializedName("ios_app_scheme")
    private String ios_app_scheme;

    // 결제 준비 요청 시간
    @SerializedName("created_at")
    private Timestamp created_at;

}
