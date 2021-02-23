package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 모달창 승인관리에서 승인대기중인 기사 눌렀을때 모달창에 붙는 기사정보
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinDriverVO {

    private String d_id;

    private String m_name;

    private String d_license_no;

    private String car_model;

    private String m_birth;
}
