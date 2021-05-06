package com.tmate.user.data;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TogetherRequest {

    String merchant_uid;
    String id;
    String m_name;
    Timestamp m_birth;
    String distance;
    int m_n_use;
    int m_t_use;
    String like;
    String dislike;
    int m_count;


}
