package com.tmate.driver.data;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class JoinBan {

    private String d_id;
    
    private String m_id;

    private String m_name;

    private String ban_reason;

    private Timestamp ban_reg_date;
}
