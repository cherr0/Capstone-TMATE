package com.tmate.domain.user;

import lombok.Data;

@Data
public class TogetherRequest {
    String merchant_uid;
    String m_id;
    String m_name;
    String m_birth;
    String distance;
    String m_t_use;
    String like;
    String dislike;
    String m_count;
}
