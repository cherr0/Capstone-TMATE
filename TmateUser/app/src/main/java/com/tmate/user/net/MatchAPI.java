package com.tmate.user.net;

import com.tmate.user.data.Approval;
import com.tmate.user.data.History;
import com.tmate.user.data.JoinHistoryVO;
import com.tmate.user.data.Together;
import com.tmate.user.data.TogetherRequest;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MatchAPI {

    @GET("match/getlist/{slttd}/{slngtd}/{flttd}/{flngtd}")
    Call<List<History>> getMatchingList(@Path("slttd") String slttd, @Path("slngtd") String slngtd, @Path("flttd") String flttd, @Path("flngtd") String flngtd);

    @GET("match/read/{merchant_uid}/{m_id}")
    Call<History> getMatchingDetail(@Path("merchant_uid") String merchant_uid, @Path("m_id") String m_id);

    @POST("match/register/matching")
    Call<Boolean> registerMatchingRegister(@Body HashMap<String, Object> hashMap);

    @GET("match/select/using/history/{m_id}")
    Call<JoinHistoryVO> getUsingHistory(@Path("m_id") String m_id);

    @DELETE("match/remove/{merchant_uid}")
    Call<Boolean> removeMatchingByMaster(@Path("merchant_uid") String merchant_uid);

    @GET("match/display/seatNum/{merchant_uid}")
    Call<List<Together>> getCurrentSeatNums(@Path("merchant_uid") String merchant_uid);

    @GET("match/display/apply/list/{merchant_uid}")
    Call<List<Approval>> displayApplyList(@Path("merchant_uid") String merchant_uid);

    @POST("match/register/apply")
    Call<Boolean> registerApply(@Body Approval approval);

    @POST("match/register/normal")
    Call<String> registerNormalMatching(@Body History history);

    @GET("match/get/approval/{merchant_uid}")
    Call<List<TogetherRequest>> getTogetherRequest(@Path("merchant_uid") String merchant_uid);

    @DELETE("match/remove/approval/{id}/{merchant_uid}")
    Call<Boolean> removeApproval(@Path("id") String id , @Path("merchant_uid") String merchant_uid);

    @POST("match/register/together")
    Call<Boolean> registerTogether(@Body Approval approval);
}
