package com.pgy.ups.pay.interfaces.form;

import com.pgy.data.handler.service.PgyDataHandlerService;
import com.pgy.data.handler.service.impl.PgyDataHandlerServiceImpl;
import org.apache.commons.lang3.StringUtils;

public class UpsUserSignForm  extends  BaseForm{


    private static final long serialVersionUID = 4881942736004919925L;

    private  String bankCard;


    private String signType;

    private String fromSystem;

    public String getBankMd5() {
        return bankMd5;
    }

    public void setBankMd5(String bankMd5) {
        this.bankMd5 = bankMd5;
    }

    private  String bankMd5;

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getBankCard() {
        return bankCard;
    }



    public String getFromSystem() {
        return fromSystem;
    }

    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }

    public void setBankCard(String bankCard) {
        if(StringUtils.isNoneBlank(bankCard)){
            PgyDataHandlerService pgyDataHandlerService = new PgyDataHandlerServiceImpl();
            this.bankMd5 = pgyDataHandlerService.md5(bankCard);
        }
        this.bankCard = bankCard;
    }



}
