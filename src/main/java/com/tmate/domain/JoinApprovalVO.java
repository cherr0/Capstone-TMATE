package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinApprovalVO {

    private String d_id;

    private String corp_code;

    private String d_license_no;

    private String car_model;
}
