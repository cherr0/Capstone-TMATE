package com.tmate.user.data;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Approval {

    private int seq;

    private String id;

    private String name;

    private Timestamp birth;

    private String appro_flag;

    private String m_id;
}
