package com.tmate.user.data;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private String m_id;
    private String m_name;
    private Timestamp m_regdate;
    private String m_imei;
    private String m_birth;
    private String m_email;
    private String m_house;
    private int m_n_use;
    private int m_t_use;
    private int m_count;
    private String m_level;
    private int m_point;
    private String m_invite;
    private String m_profile;
    private int like;
    private int dislike;
}
