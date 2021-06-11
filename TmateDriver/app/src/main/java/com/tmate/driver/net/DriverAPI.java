package com.tmate.driver.net;

import com.tmate.driver.data.AttendList;
import com.tmate.driver.data.Ban;
import com.tmate.driver.data.Car;
import com.tmate.driver.data.Driver;
import com.tmate.driver.data.DriverHistory;
import com.tmate.driver.data.DriverProfile;
import com.tmate.driver.data.JoinBan;
import com.tmate.driver.data.Member;
import com.tmate.driver.data.Review;
import com.tmate.driver.data.SidebarProfile;

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

public interface DriverAPI {
    // 회원가입
    @POST("/driver/register")
    Call<Boolean> registerDriver(@Body Map<String, String> Map);

    // 기사 승인 상태 확인
    @GET("/driver/register/approve/{d_id}")
    Call<Boolean> approveSearch(@Path("d_id") String d_id);

    // 기사 사이드바 프로필 확인
    @GET("/driver/profile/side/{d_id}")
    Call<SidebarProfile> searchSidebarProfile(@Path("d_id") String d_id);

    // 기사 프로필 확인
    @GET("/driver/profile/{d_id}")
    Call<DriverProfile> searchDriverProfile(@Path("d_id") String d_id);

    // 기사 이메일 수정
    @PUT("/driver/profile/modifyemail")
    Call<Boolean> modifyEmail(@Body Member memberDTO);

    // 기사 계좌번호 수정

    // 기사 상태 바꾸기
    @PUT("/driver/status/set/{d_id}/{d_status}")
    Call<Boolean> setStatus(@Path("d_id") String d_id,@Path("d_status") int d_status);

    // 운행기록 리스트 - GET
    @GET("/driver/history/{d_id}")
    Call<List<DriverHistory>> driveHistoryList(@Path("d_id") String d_id);

    // 운행기록 블랙리스트 추가 시 표시 데이터 리스트 - GET
    Call<List<AttendList>> historyAttendList(@Path("dp_id") String dp_id);

    // 기사 차량 리스트 - GET
    @GET("/driver/car/list/{d_id}")
    Call<List<Car>> driverCarList(@Path("d_id") String d_id);

    // 기사 차량 추가 - POST
    @POST("/driver/car")
    Call<Boolean> insertCar(@Body Car carDTO);

    // 기사 차량 선택 - PUT
    @PUT("/driver/car/{d_id}/{car_no}")
    Call<Boolean> selectCar(@Path("d_id") String d_id, @Path("car_no") String car_no);

    // 블랙리스트 확인 - GET
    @GET("/driver/ban/list/{d_id}")
    Call<List<JoinBan>> getBlacklist(@Path("d_id") String d_id);

    // 블랙리스트 추가 - POST
    @POST("/driver/ban/insert")
    Call<Boolean> addBlacklist(@Body Ban banDTO);

    // 블랙리스트 제거 - DELETE
    @DELETE("/driver/ban/delete/{d_id}/{m_id}")
    Call<Boolean> removeBlacklist(@Path("d_id")String d_id, @Path("m_id") String m_id);
}
