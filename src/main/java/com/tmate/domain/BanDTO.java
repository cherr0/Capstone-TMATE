package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanDTO {

    private String ban_id;

    private String d_id;

    private String ban_reason;

    private String m_id;
}
