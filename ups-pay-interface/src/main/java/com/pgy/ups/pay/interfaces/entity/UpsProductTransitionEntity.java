package com.pgy.ups.pay.interfaces.entity;

import com.pgy.ups.pay.interfaces.model.Model;

import javax.persistence.*;
import java.util.Date;

public class UpsProductTransitionEntity extends Model {

	private static final long serialVersionUID = 1L;


	private Long product_id;



	private Long merchant_id;


	private String product_name;


	private String product_code;


	private Boolean product_state;


	private Date add_time;


	private Date modify_time;




	public Long getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(Long merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}


	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public Boolean getProduct_state() {
		return product_state;
	}

	public void setProduct_state(Boolean product_state) {
		this.product_state = product_state;
	}

	public Date getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
}
