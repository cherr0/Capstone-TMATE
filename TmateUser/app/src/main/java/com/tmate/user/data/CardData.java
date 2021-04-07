package com.tmate.user.data;
public class CardData {
    private int cardmark;
    private String bankName;
    private String carddelete;
    private String cardNo;
    public String getCardNo() {
        return cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public int getCardmark() {
        return cardmark;
    }
    public void setCardmark(int cardmark) {
        this.cardmark = cardmark;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCarddelete() {
        return carddelete;
    }
    public void setCarddelete(String carddelete) {
        this.carddelete = carddelete;
    }
}