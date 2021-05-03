package com.tmate.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class JoinBanVO {

    private String m_id;

    private String m_name;

    private String ban_reason;

    private Timestamp ban_reg_date;
}
