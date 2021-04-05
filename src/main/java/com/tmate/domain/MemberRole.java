package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRole {

    // 0 : 유저 , 1: 매니저 , 2: 어드민
    private String m_role;

    private String m_id;

    public String Name(int m_role) {

        switch (m_role) {
            case 0:
                return "USER";
            case 1:
                return "MANAGER";
            case 2:
                return "ADMIN";
        }

        return "";
    }
}
