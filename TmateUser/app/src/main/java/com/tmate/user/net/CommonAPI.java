package com.tmate.user.net;

import com.tmate.user.data.EventDTO;
import com.tmate.user.data.LoginVO;
import com.tmate.user.data.Notice;
import com.tmate.user.data.PhoneDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommonAPI {

    // 공지사항 리스트 조회
    @GET("common/noticeList")
    Call<List<Notice>> getNoticeList();

    // 진행중인 이벤트 리스트 던지는 것
    @GET("common/eventplist")
    Call<List<EventDTO>> getEventList();

    // 공지 세부 내용 조회
    @GET("common/notice/{bd_id}")
    Call<Notice> getNoticeDetail(@Path("bd_id") String bd_id);

    // 종료된 이벤트 리스트 던지는 것
    @GET("common/eventflist")
    Call<List<EventDTO>> getFinishEventList();

    // 글 상세보기
    @GET("common/readevent/{bd_id}")
    Call<EventDTO> readEvent(@Path("bd_id") String bd_id);

    // 즐겨찾기 삭제
    @DELETE("common/deletebookmark/{bm_id}/{m_id}")
    Call<Boolean> removeBookmark(@Path("bm_id") String bm_id, @Path("m_id") String m_id);

    // 휴대폰 인증
    @POST("common/sendsms")
    Call<String> sendSMS(@Body PhoneDTO phoneDTO);

    // 로그인 체크
    @GET("common/login/{id}/{password}/{auth}")
    Call<LoginVO> loginCheck(@Path("id") String id, @Path("password") String password, @Path("auth") String auth);

    // IMEI 값 변경
    @PUT("common/imei/{m_id}/{m_imei}")
    Call<Boolean> updateIMEI(@Path("m_id")String m_id, @Path("m_imei")String m_imei);
}
