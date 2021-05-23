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
}
