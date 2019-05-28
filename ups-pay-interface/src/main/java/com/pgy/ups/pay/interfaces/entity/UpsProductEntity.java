package com.pgy.ups.pay.interfaces.entity;

import com.pgy.ups.pay.interfaces.model.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ups_t_product")
public class UpsProductEntity extends Model {

	private static final long serialVersionUID = 493808384824190908L;

	@Id
	private Long id;


	@Column(name = "merchant_id")
	private Long merchantId;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "product_code")
	private String productCode;

	@Column(name = "state")
	private Boolean status;

	@Column(name = "add_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date addTime;

	@Column(name = "modify_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;

	 public  UpsProductEntity(UpsProductTransitionEntity entity){
		this.id = entity.getProduct_id();
		this.merchantId = entity.getMerchant_id();
		this.productCode = entity.getProduct_code();
		this.productName = entity.getProduct_name();
		this.status  = entity.getProduct_state();
	}

	public  UpsProductEntity(){

	}

}
