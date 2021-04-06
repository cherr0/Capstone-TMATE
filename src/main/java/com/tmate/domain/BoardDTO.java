package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    // 이벤트 키워드 처리
    private String keyword;

    // 게시판 코드
    private String bd_id;

    // 게시판 상태
    private String bd_status;

    // 게시판 내용
    private String bd_contents;

    // 게시판 제목
    private String bd_title;

    // 조회수
    private int bd_count;

    // 작성일자
    private Timestamp bd_cre_date;

    // 수정 일자
    private Timestamp bd_mod_date;

    // 이벤트 시작 일자
    private Timestamp bd_s_date;

    // 이벤트 종료 일자
    private Timestamp bd_e_date;

    // 이벤트 이미지들
    private List<BoardImageDTO> boardImageDTOList = new ArrayList<>();


    // 다음 글 코드
    private String next_id;

    // 다음 글 타이틀
    private String next_title;

    // 다음 글 작성 시간
    private Timestamp next_cre_date;

    // 이전 글 코드
    private String prev_id;

    // 이전 글 타이틀
    private String prev_title;

    // 이전 글 작성 시간
    private Timestamp prev_cre_date;

    // 작성자 회원 코드
    private String m_id;

    /*
    * 이벤트로 넘길 URL 주소
    * */
    private String imgURL;
}
