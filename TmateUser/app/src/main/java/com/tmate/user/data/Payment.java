package com.tmate.user.data;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    // 가맹점 코드
    @SerializedName("cid")
    private String cid;

    // 기사 코드
    @SerializedName("partner_order_id")
    private String partner_order_id;

    // 회원 코드
    @SerializedName("partner_user_id")
    private String partner_user_id;

    // 상품명
    @SerializedName("item_name")
    private String item_name;

    // 상품 수량
    @SerializedName("quantity")
    private int quantity;

    // 상품 총액
    @SerializedName("total_amount")
    private int total_amount;

    // 상품 비과세 금액
    @SerializedName("tax_free_amount")
    private int tax_free_amount;

    // 결제 성공 url
    @SerializedName("approval_url")
    private String approval_url;

    // 결제 취소 url
    @SerializedName("cancel_url")
    private String cancel_url;

    // 결제 실패 url
    @SerializedName("fail_url")
    private String fail_url;
}
