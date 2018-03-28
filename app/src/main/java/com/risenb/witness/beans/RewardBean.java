package com.risenb.witness.beans;

public class RewardBean {
    private String cardId; //"cardId":3,
    private String cardName; //   "cardName":"卡券名称",
    private String cardLogo;//   "cardLogo":"卡券logo"

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardLogo() {
        return cardLogo;
    }

    public void setCardLogo(String cardLogo) {
        this.cardLogo = cardLogo;
    }
}
