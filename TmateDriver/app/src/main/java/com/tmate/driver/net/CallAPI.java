package com.tmate.driver.net;

import com.tmate.driver.data.CallHistory;
import com.tmate.driver.data.Dispatch;
import com.tmate.driver.data.HistoryData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CallAPI {

    // 기사 상태 변경
    @PUT("/call/modify/status/{d_id}/{flag}")
    Call<Boolean> modifyStatusByD_idAndFlag(@Path("d_id") String d_id, @Path("flag") int flag);

    // 2km 이내 기사 리스트 가져오기
    @GET("/call/get/callinfo/{m_lat}/{m_lng}")
    Call<List<Dispatch>> getCallInfoByPosition(@Path("m_lat") double m_lat, @Path("m_lng") double m_lng);

    // 콜 수락
    @PUT("/call/modify/dispatch/{dp_id}/{d_id}/{m_lat}/{m_lng}")
    Call<Boolean> modifyHistoryByDriver(
            @Path("dp_id") String merchant_uid,
            @Path("d_id") String d_id,
            @Path("m_lat") double m_lat,
            @Path("m_lng") double m_lng
    );

    // 승객을 태웠을 시 -> 탑승 중 , 도착지 가져온다.
    @GET("/call/modify/dispatch/boarding/{d_id}")
    Call<Dispatch> modifyDispatchBoarding(@Path("d_id") String d_id);

    // 기사 위치 최신화
    @PUT("/call/modify/driver/position/{m_lat}/{m_lng}/{d_id}")
    Call<Boolean> modifyDriverPosition(
            @Path("m_lat") double m_lat,
            @Path("m_lng") double m_lng,
            @Path("d_id") String d_id
    );

    // 네비 앱으로 넘어 갈 시 현재 이용중인 승객에게 전화하기 위해 회원코드를 반환
    @GET("/call/get/using/m_id/{d_id}")
    Call<Dispatch> getUsingM_idByD_id(@Path("d_id") String d_id);

    @PUT("/call/modify/payment/dp_status/{dp_id}/{all_fare}/{dp_status}")
    Call<Boolean> modifyPaymentDpStatus(
            @Path("dp_id") String dp_id,
            @Path("all_fare") int all_fare,
            @Path("dp_status") String dp_status
    );


    /*
    *  노쇼 관련
    * */

    // 1. 기사가 승객 노쇼 버튼 눌렀을 시 기사 APP
    @PUT("/noshow/modify/driver/noshow/{m_id}/{dp_id}")
    Call<Boolean> modifyDriverNoshow(
            @Path("m_id") String m_id,
            @Path("dp_id") String dp_id
    );



}
