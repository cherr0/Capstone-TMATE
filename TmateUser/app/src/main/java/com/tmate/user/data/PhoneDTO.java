package com.tmate.user.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDTO {

    // 이름
    private String name;

    // 휴대폰 번호
    private String phoneNumber;

    // 인증 번호
    private String confirm;
}
