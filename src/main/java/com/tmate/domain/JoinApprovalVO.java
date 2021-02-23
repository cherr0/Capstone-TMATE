package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// 승인 관리 화면 리스트에 쓰인다.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinApprovalVO {


    private String d_id;

    private String corp_code;

    private String d_license_no;

    private String car_model;
}
