package com.tmate.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class JoinReceiptVO {

    // 거래 고유 번호
    private String tid;
    // 화페 단위
    private String re_crn;
    // 결제 시간
    private String re_time;
    // 결제 금액
    private String re_amt;
    // 현금 사용 유므
    private String use_cash;
    // 카드 회사
    private String pay_company;
}
