package com.pgy.ups.pay.interfaces.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="ups_t_balance_config")
public class UpsBalanceConfigEntity  extends BaseEntity {

    private static final long serialVersionUID = 5591437914915639954L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected   Long id;

    @Column(name = "from_system")
    private String productId;



    @Column(name = "pay_channel")
    private String payChannel;

    @Column(name = "tpp_mer_no")
    private String tppMerNo;



    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPayChannel() {
        return payChannel;
    }



    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getTppMerNo() {
        return tppMerNo;
    }

    public void setTppMerNo(String tppMerNo) {
        this.tppMerNo = tppMerNo;
    }





}
