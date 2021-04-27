package com.tmate.user.net;

import com.tmate.user.data.EventDTO;
import com.tmate.user.data.Notice;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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

}
