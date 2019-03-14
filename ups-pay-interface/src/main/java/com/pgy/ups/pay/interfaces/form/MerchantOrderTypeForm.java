package com.pgy.ups.pay.interfaces.form;

import java.util.Date;

public class MerchantOrderTypeForm extends BaseForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7012778222353400375L;
	
	private Long id;

    private  Long merchantId;

    private Long orderTypeId;

    private Date startTime;

    private Date endTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getOrderTypeId() {
		return orderTypeId;
	}

	public void setOrderTypeId(Long orderTypeId) {
		this.orderTypeId = orderTypeId;
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
    
}
