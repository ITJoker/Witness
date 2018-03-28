package com.risenb.witness.beans;

public class RewardInfoBean {
    private String cardName;//"cardName":"卡券名称",
    private String cardLogo;//     "cardLogo":"卡券logo",
    private String linkUrl;//      "linkUrl":"url",
    private String usage;//       "usage":"使用方法",
    private String cardEndDate;//      "cardEndDate":"有效期至",
    private String cardNumber;//      "cardNumber":"代金券"

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

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getCardEndDate() {
        return cardEndDate;
    }

    public void setCardEndDate(String cardEndDate) {
        this.cardEndDate = cardEndDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
