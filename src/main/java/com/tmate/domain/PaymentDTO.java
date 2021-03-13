package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    /*
    * CUSTOMER_UID NOT NULL VARCHAR2(21)
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

    // 회원 번호
    private String m_id;

    // pg사
    private String pay_pg;

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
}
