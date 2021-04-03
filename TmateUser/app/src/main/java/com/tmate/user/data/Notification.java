package com.tmate.user.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private String n_phone;

    private String m_id;

    private String n_name;

    private String n_whether;
}
