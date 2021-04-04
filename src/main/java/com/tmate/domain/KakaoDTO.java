package com.tmate.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class KakaoDTO {

    // 거래 고유 번호
    private String tid;

    // 요청 고유 번호
    private String aid;

    // 화폐 단위
    private String re_crn;

    // 결제 시간
    private Timestamp approved_at;

    // 금액
    private int re_amt;

    // 현금 결제 유무
    private String use_cash;

    // 빌링키
    private String customer_uid;

    // 회원코드
    private String m_id;

    // 기사코드
    private String d_id;
}
