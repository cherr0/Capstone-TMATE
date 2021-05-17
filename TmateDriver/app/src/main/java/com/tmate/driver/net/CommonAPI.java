package com.tmate.driver.net;

import com.tmate.driver.data.Event;
import com.tmate.driver.data.LoginVO;
import com.tmate.driver.data.Notice;
import com.tmate.driver.data.Phone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommonAPI {
    // 공지사항 리스트 조회
    @GET("/common/noticeList")
    Call<List<Notice>> getNoticeList();

    // 공지사항 세부 사항
    @GET("/common/notice/{bd_id}")
    Call<Notice> getNoticeDetail(@Path("bd_id") String bd_id);

    // 진행중 이벤트 리스트
    @GET("/common/eventplist")
    Call<List<Event>> getEventList();

    // 종료된 이벤트 리스트
    @GET("/common/eventflist")
    Call<List<Event>> getFinishEventList();

    // 이벤트 세부 사항
    @GET("/common/readevent/{bd_id}")
    Call<Event> readEvent(@Path("bd_id") String bd_id);

    // 북마크 삭제
    @DELETE("/common/deletebookmark/{bm_id}/{m_id}")
    Call<Boolean> removeBookmark(@Path("bm_id") String bm_id, @Path("m_id") String m_id);

    // 휴대폰 인증
    @POST("/common/sendsms")
    Call<String> sendSMS(@Body Phone phoneDTO);

    // 로그인 체크
    @GET("common/login/{id}/{password}/{auth}")
    Call<LoginVO> loginCheck(@Path("id") String id, @Path("password") String password, @Path("auth") String auth);
}
