package com.tmate.user.Fragment;

import com.tmate.user.data.FavoritesData;
import com.tmate.user.data.Approval;
import com.tmate.user.data.CardData;
import com.tmate.user.data.Member;
import com.tmate.user.data.Notification;
import com.tmate.user.data.PhoneDTO;
import com.tmate.user.data.PointData;
import com.tmate.user.data.Social;
import com.tmate.user.data.UserHistroy;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class DataService {

    /* ------------------------
             서버 연결
      ------------------------ */
    private String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/member/"; // 기본 URL
//    private String BASE_URL = "http://10.0.2.2:9090/member/";

    Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public FriendAPI friend = retrofitClient.create(FriendAPI.class);

    public ProfileAPI profile = retrofitClient.create(ProfileAPI.class);

    public PointAPI point = retrofitClient.create(PointAPI.class);

    public CardAPI card = retrofitClient.create(CardAPI.class);

}

interface FriendAPI {
    // 내가 지인에게 승인을 요청할 시
    @POST("approval")
    Call<Boolean> approvalFriend(@Body Approval approval);

    // 지인 알림 -> 나의 지인들
    @GET("friend/{m_id}")
    Call<List<Notification>> friendByUser(@Path("m_id") String m_id);

    // 지인 알림 -> 검색 부분
    @POST("search")
    Call<List<Member>> searchMembers(@Body String phoneNo);

    // 지인 알림 -> 나에게 요청한 리스트
    @GET("myapproval/{m_id}")
    Call<List<Approval>> myApprovalList(@Path("m_id") String m_id);

}

interface ProfileAPI {
    // 회원가입
    @POST("register")
    Call<Boolean> insertOne(@Body Map<String, String> map);

    // 소셜 로그인 연동
    @POST("social")
    Call<Boolean> socialAccount(@Body Social social);

    // 상세 프로필 정보
    @GET("select/{m_id}")
    Call<Member> selectProfile(@Path("m_id") String m_id);

    // 유저 이용 내역 정보
    @GET("historys/{m_id}")
    Call<List<UserHistroy>> selectHistory(@Path("m_id") String m_id);

    // 휴대폰 인증
    @POST("sendsms")
    Call<Boolean> sendSMS(@Body PhoneDTO phoneDTO);

    // 휴대폰 인증 정보 확인
    @POST("confirm")
    Call<Integer> confirm(@Body PhoneDTO phoneDTO);

    // 즐겨 찾기 -> 즐겨찾기 리스트 가져오기
    @GET("bookmark/{m_id}")
    Call<List<FavoritesData>> getBookmarkList(@Path("m_id") String m_id);

}

interface PointAPI {

    // 포인트 -> 잔여 포인트와 포인트 리스트 가져오기
    @GET("point/{m_id}")
    Call<List<PointData>> getPointList(@Path("m_id") String m_id);

}

interface CardAPI {

    // 카드 추가
    @POST("regcard")
    Call<Boolean> registerCard(@Body CardData cardData);

    // 카드 리스트
    @GET("card/{m_id}")
    Call<List<CardData>> getUserCard(@Path("m_id") String m_id);

}