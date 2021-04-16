package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanDTO {

    private String ban_id;

    private String d_id;

    private String ban_reason;

    private Timestamp ban_regdate;

    private String m_id;
}
