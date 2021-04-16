package com.tmate.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalDTO {

    /*
    *  사용처 : 지인알림 신청 리스트(WEb , App) 와 동승 신청 리스트에서 사용한다. (App)
    * */

    // 시퀀스
    private int seq;

    // 신청을 한 사람의 아이디 -> 지인 알림, 동승 신청
    private String id;

    // 동승 신청을 한 방의 코드 -> merchant_uid
    private String merchant_uid;

    // 신청을 한 사람의 이름
    private String name;

    // 신청을 한 사람의 주소
    private String sum_address;

    // 신청을 한 사람의 생년 원일
    private Timestamp birth;

    // 동승 신청 한것인지(0) , 알림 신청 한것인지 여부(1)
    private String appro_flag;

    // 신청을 받은 사람의 회원 코드
    private String m_id;
}
