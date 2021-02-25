package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

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

    // 연관관계
    // 회원 코드 아마 관리자
    private String m_id;



}
