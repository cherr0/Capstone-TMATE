package com.tmate.user.data;

import lombok.Data;

@Data
public class CardData {
    /*
    *
SID          NOT NULL VARCHAR2(21)
M_ID         NOT NULL VARCHAR2(14)
PAY_PG                VARCHAR2(15)
PAY_REP      NOT NULL CHAR(1)
PAY_COMPANY           VARCHAR2(15)
CREDIT_NO             VARCHAR2(32)
CREDIT_PW             VARCHAR2(32)
CREDIT_CVC            VARCHAR2(32)
CREDIT_VALI           VARCHAR2(32)
PAY_COM_REG           CHAR(10)
    * */

    // 결제 고유 번호
    private String sid;

    // 회원 번호
    private String m_id;

    // pg사
    private String pay_pg;

    // 대표카드 여부
    //private String pay_rep;

    // 회사
    private String pay_company;

    // 신용 카드
    private String credit_no;

    // 비밀번호
    private String credit_pw;

    // cvc
    private String credit_cvc;

    // 유효 일자
    private String credit_vali;

    // 사업자 등록 번호
    private String pay_com_reg;

    // 카드 회사명
    private String kakaopay_purchase_corp;

    // 카드 타입
    private String card_type;

    // 카드 별칭
    private String pay_alias;

    // 카드 활성화 비활성화
    private String active;
}