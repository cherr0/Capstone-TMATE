package com.tmate.driver.adapter;

import com.tmate.driver.data.Ban;
import com.tmate.driver.data.Car;
import com.tmate.driver.data.Driver;
import com.tmate.driver.data.DriverHistory;
import com.tmate.driver.data.DriverProfile;
import com.tmate.driver.data.JoinBan;
import com.tmate.driver.data.Member;
import com.tmate.driver.data.Review;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class DataService {


    /* ------------------------
             서버 연결
      ------------------------ */
    private String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/driver/"; // 기본 URL
//    private String BASE_URL = "http://10.0.2.2:9090/member/"; // 로컬 URL

    Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}

// 회원가입, 프로필 관련 API
interface ProfileAPI {
    // 회원가입
    @POST("register")
    Call<Boolean> registerDriver(@Body Member memberDTO, @Body Driver driverDTO);

    // 기사 프로필 확인
    @GET("profile/{d_id}")
    Call<DriverProfile> searchDriverProfile(@Path("d_id") String d_id);

    // 기사 이메일 수정
    @PUT("profile/modifyemail")
    Call<Boolean> modifyEmail(@Body Member memberDTO);

    // 기사 계좌번호 수정

    // 기사 상태 바꾸기
    @PUT("status/set")
    Call<Boolean> setStatus(@Body Driver driverDTO);
}

// 운행 기록 관련 API
interface HistoryAPI {
    // 운행기록 리스트 - GET
    @GET("history/{d_id}")
    Call<List<DriverHistory>> driveHistoryList(@Path("d_id") String d_id);

    // 리뷰 리스트 - GET
    @GET("history/{merchant_uid}")
    Call<List<Review>> reviewList(@Path("merchant_uid") String merchant_uid);

}

// 차량 관련 API
interface CarAPI {
    // 기사 차량 리스트 - GET
    @GET("car/list/{d_id}")
    Call<List<Car>> driverCarList(@Path("d_id") String d_id);

    // 기사 차량 추가 - POST
    @POST("car")
    Call<Boolean> insertCar(@Body Car carDTO);
}

// 블랙리스트 관련 API
interface BanAPI {
    // 블랙리스트 확인 - GET
    @GET("ban/list/{d_id}")
    Call<List<JoinBan>> getBlacklist(@Path("d_id") String d_id);

    // 블랙리스트 추가 - POST
    @POST("ban/insert")
    Call<Boolean> addBlacklist(@Body Ban banDTO);

    // 블랙리스트 제거 - DELETE
    @DELETE("ban/delete")
    Call<Boolean> removeBlacklist(@Body Ban banDTO);

}
