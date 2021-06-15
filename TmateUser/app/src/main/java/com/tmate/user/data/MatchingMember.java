package com.tmate.user.data;

import java.sql.Timestamp;

public class MatchingMember {
    String dp_id;
    String m_id;
    String m_name;
    Timestamp m_birth;
    String m_t_use;
    String like;
    String dislike;
    String m_count;

    public String getDp_id() {
        return dp_id;
    }

    public void setDp_id(String dp_id) {
        this.dp_id = dp_id;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public Timestamp getM_birth() {
        return m_birth;
    }

    public void setM_birth(Timestamp m_birth) {
        this.m_birth = m_birth;
    }

    public String getM_t_use() {
        return m_t_use;
    }

    public void setM_t_use(String m_t_use) {
        this.m_t_use = m_t_use;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public String getM_count() {
        return m_count;
    }

    public void setM_count(String m_count) {
        this.m_count = m_count;
    }
}
