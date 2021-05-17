package com.tmate.driver.net;

import com.tmate.driver.data.CallHistory;
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
    @GET("/call/get/callinfo/{m_lttd}/{m_lngtd}")
    Call<List<CallHistory>> getCallInfoByPosition(@Path("m_lttd") double m_lttd, @Path("m_lngtd") double m_lngtd);

    // 콜 수락
    @PUT("/call/modify/history/{merchant_uid}/{d_id}/{m_lttd}/{m_lngtd}")
    Call<Boolean> modifyHistoryByDriver(
            @Path("merchant_uid") String merchant_uid,
            @Path("d_id") String d_id,
            @Path("m_lttd") double m_lttd,
            @Path("m_lngtd") double m_lngtd
    );
}
