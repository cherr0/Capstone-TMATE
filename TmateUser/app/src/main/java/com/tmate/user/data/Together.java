package com.tmate.user.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Together {

    private String m_id;

    private String merchant_uid;

    private int to_max;

    private int to_seat;

}
