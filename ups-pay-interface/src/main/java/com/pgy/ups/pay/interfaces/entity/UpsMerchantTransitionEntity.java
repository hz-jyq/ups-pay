package com.pgy.ups.pay.interfaces.entity;

import com.pgy.ups.pay.interfaces.model.Model;


import java.util.List;

public class UpsMerchantTransitionEntity extends Model {


    private Long merchant_id;

    private String merchant_no;

    private String merchant_name;

    private Boolean merchant_state;


    private List<UpsProductTransitionEntity> product_list;

    public Boolean getMerchant_state() {
        return merchant_state;
    }

    public void setMerchant_state(Boolean merchant_state) {
        this.merchant_state = merchant_state;
    }


    public List<UpsProductTransitionEntity> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<UpsProductTransitionEntity> product_list) {
        this.product_list = product_list;
    }

    public Long getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(Long merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }


}
