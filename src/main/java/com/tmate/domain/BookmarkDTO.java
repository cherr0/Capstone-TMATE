package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDTO {

    // 장소 코드
    private int bm_id;

    // 회원 코드
    private String m_id;


    // 장소명
    private String bm_name;

    // 즐겨찾기 등록 일자
    private Timestamp bm_date;

    // 위도
    private int bm_lttd;

    // 경도
    private int bm_lngtd;

}
