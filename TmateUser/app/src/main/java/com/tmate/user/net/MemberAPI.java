package com.tmate.user.net;

import com.tmate.user.data.Approval;
import com.tmate.user.data.CardData;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.Dv_option;
import com.tmate.user.data.FavoritesData;
import com.tmate.user.data.Member;
import com.tmate.user.data.Notice;
import com.tmate.user.data.Notification;
import com.tmate.user.data.PhoneDTO;
import com.tmate.user.data.Place;
import com.tmate.user.data.PointData;
import com.tmate.user.data.Social;
import com.tmate.user.data.UserHistroy;

import java.sql.Timestamp;
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

    // 지인 추가
    @POST("member/register/friend")
    Call<Boolean> registerFriend(@Body Notification notification);

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

    // 지인 삭제
    @DELETE("member/remove/friend/{m_id}/{n_name}")
    Call<Boolean> removeFriend(@Path("m_id") String m_id, @Path("n_name") String n_name);

    // 요청 거부
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

    // 회원 생년 월일 정보
    @GET("member/get/birth/{m_id}")
    Call<String> getBirth(@Path("m_id") String m_id);

    // 유저 이용 내역 정보
    @GET("member/historys/{m_id}")
    Call<List<Dispatch>> selectHistory(@Path("m_id") String m_id);
    
    // 즐겨 찾기 -> 즐겨찾기 리스트 가져오기
    @GET("member/bookmark/{m_id}")
    Call<List<FavoritesData>> getBookmarkList(@Path("m_id") String m_id);

    // 즐겨찾기 등록
    @POST("member/bookmark")
    Call<Boolean> insertBookmark(@Body FavoritesData favoritesData);

    // 즐겨찾기 삭제
    @DELETE("member/bookmark/{bm_name}/{m_id}")
    Call<Boolean> deleteBookmark(@Path("bm_name") String bm_name, @Path("m_id") String m_id);

    /*
    * 포인트 관련 서비스
    * */

    @GET("member/point/{m_id}")
    Call<List<PointData>> getPointList(@Path("m_id") String m_id);

    @GET("member/unusedPoint/{m_id}")
    Call<Integer> getUnusedPoint(@Path("m_id") String m_id);

    @POST("member/register/point")
    Call<Boolean> registerPoint(@Body PointData pointData);

    @GET("member/get/useCount/{m_id}")
    Call<Integer> getUseCount(@Path("m_id") String m_id);

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
    @DELETE("member/remove/{sid}")
    Call<Boolean> removeCard(@Path("sid") String sid);

    /*
    * 운행 옵션
    * */

    // 운행 옵션 목록 가져오기
    @GET("member/get/dvoption/{m_id}")
    Call<Dv_option> getDvOptionByM_id(@Path("m_id") String m_id);

    // 운행 옵션 업데이트
    @PUT("member/modify/dvoption")
    Call<Boolean> modifyDvOption(@Body Dv_option dv_option);

    // 메인 뷰 최신 공지 리스트 가져오기
    @GET("member/board/mainnotice")
    Call<List<Notice>> getMainNoticeList();

    // 핫플레이스 조회
    @GET("/member/hotplace")
    Call<List<Place>> getPlaceList();

    // 핫플레이스 목적지 횟수 업데이트
    @PUT("/member/hotplace/{pl_id}")
    Call<Boolean> updatePlaceCnt(@Path("pl_id") String pl_id);

}
