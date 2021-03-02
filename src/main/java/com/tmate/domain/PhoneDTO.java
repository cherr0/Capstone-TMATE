package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDTO {

    // 이름
    private String name;

    // 휴대폰 번호
    private String phoneNumber;

    // 인증번호
    private String confirm;

}
