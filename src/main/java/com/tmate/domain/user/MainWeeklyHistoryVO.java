package com.tmate.domain.user;

import lombok.Data;

// 사용자 일주일간 이용 내역
@Data
public class MainWeeklyHistoryVO {

    private String merchant_uid;

    private String h_allfare;

    private String h_s_place;

    private String h_f_place;

    private String car_no;

    private String car_model;
}
