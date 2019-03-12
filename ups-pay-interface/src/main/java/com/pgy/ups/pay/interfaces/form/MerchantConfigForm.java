package com.pgy.ups.pay.interfaces.form;

import java.util.Date;

public class MerchantConfigForm extends BaseForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5539785880156428493L;

	private Long id;

	private String merchantCode;

	private String merchantName;

	private String description;

	private Boolean available;

	private Date startTime;

	private Date endTime;

	private String merchantPublicKey;

	private String upsPrivateKey;

	private Date createTime;

	private Date updateTime;

	private String createUser;

	private String updateUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getMerchantPublicKey() {
		return merchantPublicKey;
	}

	public void setMerchantPublicKey(String merchantPublicKey) {
		this.merchantPublicKey = merchantPublicKey;
	}

	public String getUpsPrivateKey() {
		return upsPrivateKey;
	}

	public void setUpsPrivateKey(String upsPrivateKey) {
		this.upsPrivateKey = upsPrivateKey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}	

}
