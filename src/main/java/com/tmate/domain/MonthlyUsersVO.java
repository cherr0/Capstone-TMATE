package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 월별 이용자
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyUsersVO {

    // 기준
    private String standard;

    // 이용 수
    private int users;
}
