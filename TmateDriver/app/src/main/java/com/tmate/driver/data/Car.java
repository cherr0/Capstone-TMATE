package com.tmate.driver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    // 차량 번호
    private String car_no;

    // 차대 번호
    private String car_vin;

    // 차종
    private String car_model;

    // 차량 분류
    private String car_kind;

    // 차량 색상
    private String car_color;

    // 회원 코드
    private String m_id;


}
