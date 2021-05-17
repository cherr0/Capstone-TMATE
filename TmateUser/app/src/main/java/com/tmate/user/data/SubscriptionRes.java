package com.tmate.user.data;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRes {
    @SerializedName("aid")
    private String aid; // Request 고유 번호

    @SerializedName("cid")
    private String cid; // 가맹점 코드

    @SerializedName("sid")
    private String sid; // 정기 결제 고유 번

    @SerializedName("tid")
    private String tid; // 결제 고유 번호

    @SerializedName("partner_order_id")
    private String partner_order_id; // 가맹점 주문 번호

    @SerializedName("partner_user_id")
    private String partner_user_id; // 가맹점 회원 id

    @SerializedName("payment_method_type")
    private String payment_method_type; // 결제 수단 (MONEY or CARD)

    @SerializedName("item_name")
    private String item_name; // 상품 이름

    @SerializedName("quantity")
    private int quantity; // 상품 수량

    @SerializedName("amount")
    private Amount amount; // 결제 금액 정보

    @SerializedName("card_info")
    private CardInfo card_info; // 결제 수단이 카드일 경우 결제 상세 정보

    @SerializedName("created_at")
    private Timestamp created_at; // 결제 준비 요청 시각

    @SerializedName("approved_at")
    private Timestamp approved_at; // 결제 승인 시각

    @Data
    static class Amount {
        @SerializedName("total")
        private int total; // 전체 결제 금액

        @SerializedName("tax_free")
        private int tax_free; // 비과세 금액

        @SerializedName("vat")
        private int vat; // 부가세 금액

        @SerializedName("discount")
        private int discount; // 할인 금액

        @SerializedName("point")
        private int point; // 포인트 금액

    }

    @Data
    static class CardInfo {
        @SerializedName("purchase_corp")
        private String purchase_corp; // 매입 카드사 한글명

        @SerializedName("purchase_corp_code")
        private String purchase_corp_code; // 매입 카드사 코드

        @SerializedName("issuer_corp")
        private String issuer_corp; // 카드 발급사 한글명

        @SerializedName("issuer_corp_code")
        private String issuer_corp_code; // 카드 발급사 코드

        @SerializedName("kakaopay_purchase_corp")
        private String kakaopay_purchase_corp; // 카카오페이 매입사명

        @SerializedName("kakaopay_purchase_corp_code")
        private String kakaopay_purchase_corp_code; // 카카오페이 매입사 코드

        @SerializedName("kakaopay_issuer_corp")
        private String kakaopay_issuer_corp; // 카카오페이 발급사명

        @SerializedName("kakaopay_issuer_corp_code")
        private String kakaopay_issuer_corp_code; // 카카오페이 발급사 코드

        @SerializedName("bin")
        private String bin; // 카드 bin

        @SerializedName("card_type")
        private String card_type; // 카드 타입

        @SerializedName("install_month")
        private String install_month; // 할부 개월 수

        @SerializedName("approved_id")
        private String approved_id; // 카드사 승인번호

        @SerializedName("card_mid")
        private String card_mid; // 카드사 가맹점 번호
    }
}
