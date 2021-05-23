package com.tmate.user.net;

import com.tmate.user.data.Approval;
import com.tmate.user.data.Dispatch;
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

    @DELETE("match/remove/{merchant_uid}")
    Call<Boolean> removeMatchingByMaster(@Path("merchant_uid") String merchant_uid);

    @GET("match/display/seatNum/{merchant_uid}")
    Call<List<Together>> getCurrentSeatNums(@Path("merchant_uid") String merchant_uid);

    @GET("match/display/apply/list/{merchant_uid}")
    Call<List<Approval>> displayApplyList(@Path("merchant_uid") String merchant_uid);

    @POST("match/register/apply")
    Call<Boolean> registerApply(@Body Approval approval);

    @GET("match/get/approval/{merchant_uid}")
    Call<List<TogetherRequest>> getTogetherRequest(@Path("merchant_uid") String merchant_uid);

    @DELETE("match/remove/approval/{id}/{merchant_uid}")
    Call<Boolean> removeApproval(@Path("id") String id , @Path("merchant_uid") String merchant_uid);

    @POST("match/register/together")
    Call<Boolean> registerTogether(@Body Approval approval);


    // 일반 호출
    @POST("match/register/normal")
    Call<String> registerNormalMatching(@Body Dispatch dispatch);

    // 기사를 찾는다. CallWaitingActivity에서
    @GET("match/get/driver/{dp_id}")
    Call<String> getd_idDuringCall(@Path("dp_id") String dp_id);

    // 호출 취소를 누른다. -> CallWaitingActivity에서 뒤로가기 누를 때
    @DELETE("match/remove/normal/call/{dp_id}")
    Call<Boolean> removeNormalCall(@Path("dp_id") String dp_id);

    // 일반 호출 상세 정보
    @GET("match/read/dispatch/{dp_id}")
    Call<Dispatch> readCurrentDispatch(@Path("dp_id") String dp_id);

    // 이용중인 호출 요약 정보
    @GET("match/get/dispatch/{m_id}")
    Call<Dispatch> getUsingHistory(@Path("m_id") String m_id);

}
