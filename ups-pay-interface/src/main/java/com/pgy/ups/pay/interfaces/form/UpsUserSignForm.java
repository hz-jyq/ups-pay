package com.pgy.ups.pay.interfaces.form;

public class UpsUserSignForm  extends  BaseForm{


    private static final long serialVersionUID = 4881942736004919925L;

    private  String bankCard;



    private String orderType;

    private String fromSystem;


    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getFromSystem() {
        return fromSystem;
    }

    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }




}
