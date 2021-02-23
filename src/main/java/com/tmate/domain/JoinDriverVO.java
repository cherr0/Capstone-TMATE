package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
