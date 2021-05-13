package com.tmate.service.android.common;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.LoginVO;

import java.util.List;
import java.util.Map;

public interface CommonService {

    // 공지사항

    /*
     * 이벤트
     * 이벤트 리스트 불러오기(진행중인 이벤트 , 종료된 이벤트)
     * 이벤트 상세 페이지
     * */
    // 진행중인 이벤트
    public List<BoardDTO> getProgressEventList();

    // 종료된 이벤트
    public List<BoardDTO> getFinishedEventList();


    public BoardDTO readEvent(String bd_id);

    // FAQ


    // 1대1 문의하기
    // 챗봇

    // 로그인
    public LoginVO userLogin(String id, String password, String auth);

}
