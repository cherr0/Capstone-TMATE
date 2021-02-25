package com.tmate.domain;

import lombok.Data;

// 연령별 유저 수 VO
@Data
public class UsersByAgeVO {

    // 연령대
    private int ageGroup;

    // 연령대별 수
    private int count;
}
