package com.tmate.user.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

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

    // 작성자 회원 코드
    private String m_id;
}
