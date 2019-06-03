package com.pgy.ups.pay.interfaces.model;

import com.pgy.ups.pay.interfaces.common.Signable;

public class OrderPushModel extends Model implements Signable {

	private static final String[] SIGN_RULE = new String[] { "productId", "payChannel", "orderType", "upsOrderId",
			"orderStatus", "channelResultMsg", "channelResultCode", "bussinessFlowNum","remark"};

	/**
	 * 
	 */
	private static final long serialVersionUID = -8658858942148899154L;

	private Long productId;

	private String payChannel;

	private String orderType;

	private Long upsOrderId;

	private String orderStatus;

	private String channelResultMsg;

	private String channelResultCode;

	private String bussinessFlowNum;



	private String sign;


	public  String remark;

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Long getUpsOrderId() {
		return upsOrderId;
	}

	public void setUpsOrderId(Long upsOrderId) {
		this.upsOrderId = upsOrderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getChannelResultMsg() {
		return channelResultMsg;
	}

	public void setChannelResultMsg(String channelResultMsg) {
		this.channelResultMsg = channelResultMsg;
	}

	public String getChannelResultCode() {
		return channelResultCode;
	}

	public void setChannelResultCode(String channelResultCode) {
		this.channelResultCode = channelResultCode;
	}

	public String getBussinessFlowNum() {
		return bussinessFlowNum;
	}

	public void setBussinessFlowNum(String bussinessFlowNum) {
		this.bussinessFlowNum = bussinessFlowNum;
	}




	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}


	public Long getProductId() {
		return productId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String[] getSignRule() {

		return SIGN_RULE;
	}

}
