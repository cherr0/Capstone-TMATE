package com.tmate.user.data;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class InactiveRes {
    // 정기 결제 비활성화

    @SerializedName("cid")
    private String cid; // 가맹점 코드

    @SerializedName("sid")
    private String sid; // 정기결제 고유 번호

    @SerializedName("status")
    private String status; // 정기결제 상태, Active(활성) 또는 Inactive(비활성) 중 하나

    @SerializedName("created_at")
    private Timestamp created_at; // SID 발급 시각

    @SerializedName("last_approved_at")
    private Timestamp last_approved_at; // 마지막 결제 승인 시각

    @SerializedName("inactivated_at")
    private Timestamp inactivated_at; // 정기 결제 비활성화 시각
}
