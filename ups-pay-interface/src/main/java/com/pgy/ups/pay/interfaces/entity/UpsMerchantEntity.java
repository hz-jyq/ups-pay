package com.pgy.ups.pay.interfaces.entity;

import com.pgy.ups.pay.interfaces.model.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ups_t_merchant")
public class UpsMerchantEntity extends Model {

	private static final long serialVersionUID = 493808384824190908L;

	@Id
	private Long id;


	@Column(name = "merchant_no")
	private String merchantNo;

	@Column(name = "merchant_name")
	private String merchantName;

	@Column(name = "state")
	private Boolean status;

	@Column(name = "add_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date addTime;

	@Column(name = "modify_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public  UpsMerchantEntity(UpsMerchantTransitionEntity entity){
		this.id = entity.getMerchant_id();
		this.merchantNo = entity.getMerchant_no();
		this.merchantName = entity.getMerchant_name();
		this.status  = entity.getMerchant_state();
	}


	public  UpsMerchantEntity(){

	}
}
