package com.tmate.user.net;

import com.tmate.user.data.Approval;
import com.tmate.user.data.CardData;
import com.tmate.user.data.Dv_option;
import com.tmate.user.data.FavoritesData;
import com.tmate.user.data.Member;
import com.tmate.user.data.Notification;
import com.tmate.user.data.PhoneDTO;
import com.tmate.user.data.PointData;
import com.tmate.user.data.Social;
import com.tmate.user.data.UserHistroy;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MemberAPI {

    /*
    *  지인 알림 부분
    * */

    // 내가 지인에게 승인을 요청할 시
    @POST("member/approval")
    Call<Boolean> approvalFriend(@Body Approval approval);

    // 지인 알림 -> 나의 지인들
    @GET("member/friend/{m_id}")
    Call<List<Notification>> friendByUser(@Path("m_id") String m_id);

    // 지인 알림 -> 검색 부분
    @POST("member/search")
    Call<List<Member>> searchMembers(@Body String phoneNo);

    // 지인 알림 -> 나에게 요청한 리스트
    @GET("member/myapproval/{m_id}")
    Call<List<Approval>> myApprovalList(@Path("m_id") String m_id);

    // 요청 승인
    @PUT("member/agree/{m_id}")
    Call<Boolean> agreeAppro(@Path("m_id") String m_id, @Body Notification notification);

    // 활성화&비활성화 상태
    @PUT("member/updatestat")
    Call<Boolean> modifyStat(@Body Notification notification);

    // 요청 거
    @DELETE("member/removeAppro/{id}/{m_id}")
    Call<Boolean> removeApproval(@Path("id") String id, @Path("m_id") String m_id);


    /*
    * 회원 가입 부터 프로필 상세 부분
    * */

    // 회원가입
    @POST("member/register")
    Call<Boolean> insertOne(@Body Map<String, String> map);

    // 소셜 로그인 연동
    @POST("member/social")
    Call<Boolean> socialAccount(@Body Social social);

    // 상세 프로필 정보
    @GET("member/select/{m_id}")
    Call<Member> selectProfile(@Path("m_id") String m_id);

    // 유저 이용 내역 정보
    @GET("member/historys/{m_id}")
    Call<List<UserHistroy>> selectHistory(@Path("m_id") String m_id);

    // 휴대폰 인증
    @POST("member/sendsms")
    Call<Boolean> sendSMS(@Body PhoneDTO phoneDTO);

    // 휴대폰 인증 정보 확인
    @POST("member/confirm")
    Call<Integer> confirm(@Body PhoneDTO phoneDTO);

    // 즐겨 찾기 -> 즐겨찾기 리스트 가져오기
    @GET("member/bookmark/{m_id}")
    Call<List<FavoritesData>> getBookmarkList(@Path("m_id") String m_id);


    /*
    * 포인트 관련 서비스
    * */

    @GET("point/{m_id}")
    Call<List<PointData>> getPointList(@Path("m_id") String m_id);

    /*
    *  카드 관련 서비스
    * */
    // 카드 추가
    @POST("member/regcard")
    Call<Boolean> registerCard(@Body CardData cardData);

    // 카드 리스트
    @GET("member/card/{m_id}")
    Call<List<CardData>> getUserCard(@Path("m_id") String m_id);

    // 카드 삭제
    @DELETE("member/remove/{customer_uid}")
    Call<Boolean> removeCard(@Path("customer_uid") String customer_uid);

    // 카드 대표
    @PUT("member/updaterep/{customer_uid}/{m_id}")
    Call<Boolean> modifyRep(@Path("customer_uid") String customer_id, @Path("m_id") String m_id);

    /*
    * 운행 옵션
    * */

    // 운행 옵션 목록 가져오기
    @GET("member/get/dvoption/{m_id}")
    Call<Dv_option> getDvOptionByM_id(@Path("m_id") String m_id);

    // 운행 옵션 업데이트
    @PUT("member/modify/dvoption")
    Call<Boolean> modifyDvOption(@Body Dv_option dv_option);


}
