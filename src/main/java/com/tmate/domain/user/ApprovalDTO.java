package com.tmate.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalDTO {

    private int seq;

    private String id;

    private String name;

    private Timestamp birth;

    private String sum_address;

    private String appro_flag;

    private String m_id;
}
