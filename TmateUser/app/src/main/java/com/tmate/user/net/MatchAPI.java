package com.tmate.user.net;

import com.tmate.user.data.Approval;
import com.tmate.user.data.Attend;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.History;
import com.tmate.user.data.JoinHistoryVO;
import com.tmate.user.data.Member;
import com.tmate.user.data.ReviewVO;
import com.tmate.user.data.Together;
import com.tmate.user.data.TogetherRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    // 기사 위치 가져온다 - 쓰레드
    @GET("match/get/driver/position/{dp_id}")
    Call<Dispatch> getDriverPosition(@Path("dp_id") String d_id);

    // 동승 호출

    // 출발지 800m, 목적지 가까운 순으로 리스트 뽑아오기
    @GET("match/get/together/list/{s_lat}/{s_lng}/{f_lat}/{f_lng}")
    Call<List<Dispatch>> getTogetherList(
            @Path("s_lat") double s_lat,
            @Path("s_lng") double s_lng,
            @Path("f_lat") double f_lat,
            @Path("f_lng") double f_lng);

    // 맘에 드는 리스트가 없을 시 자기가 방을 만든다.
    @POST("match/register/together/match")
    Call<String> registerTogetherMatch(@Body Map<String, Object> hashmap);

    // 배차 정보 삭제
    @DELETE("match/remove/together/match/{dp_id}")
    Call<Boolean> removeTogetherMatch(@Path("dp_id") String dp_id);

    // 동승 참가 버튼
    @POST("match/register/apply/match")
    Call<Boolean> registerApplyMatch(@Body Attend attend);

    // 동승 거절 버튼
    @PUT("match/reject/apply/match/{dp_id}/{m_id}")
    Call<Boolean> rejectApplyMatch(
            @Path("dp_id") String dp_id,
            @Path("m_id") String m_id
    );

    // 동승 수락 버튼
    @PUT("match/agree/apply/match/{dp_id}/{m_id}")
    Call<Boolean> agreeApplyMatch(
            @Path("dp_id") String dp_id,
            @Path("m_id") String m_id
    );

    // 동승자 신청 리스트
    @GET("match/get/applyer/list/{dp_id}")
    Call<List<Attend>> getApplyerList(@Path("dp_id") String dp_id);

    // 동승자 정보들
    @GET("match/get/passenger/list/{dp_id}")
    Call<List<Attend>> getPassengerList(@Path("dp_id") String dp_id);

    // 이미 참가된 승객들의 좌석 보여주기
    @GET("match/get/choice/seat/{dp_id}")
    Call<List<Attend>> getChoiceSeatNo(@Path("dp_id") String dp_id);

    // 리뷰 데이터 업데이트
    @PUT("match/finish/review")
    Call<Boolean> updateReview(@Body ReviewVO reviewVO);

}
