package com.tmate.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class JoinDriverProfileVO {

    private String m_id;

    private String m_name;

    private String m_profile;

    private Timestamp m_birth;

    private String d_license_no;

    private Timestamp d_j_date;

    private String ac_num;

    private String car_no;

    private String car_model;
}