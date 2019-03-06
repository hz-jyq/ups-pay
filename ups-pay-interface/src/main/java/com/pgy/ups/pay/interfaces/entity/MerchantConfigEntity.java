package com.pgy.ups.pay.interfaces.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.pgy.ups.pay.interfaces.model.Model;
/**
 * 来源商户配置表
 * @author 墨凉
 *
 */
@Entity
@Table(name="ups_t_merchant_config")
public class MerchantConfigEntity extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1205296307960629481L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="merchant_code")
	private String merchantCode;
	
	@Column(name="merchant_name")
	private String merchantName;
	
	@Column(name="available",columnDefinition="bit")
	private Boolean available;
	
	@Column(name="start_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name="end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@Column(name="merchant_public_key")
	private String merchantPublicKey;
	
	@Column(name="ups_private_key")
	private String upsPrivateKey;
	
	@Column(name="create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(name="update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	
	@Column(name="create_user")
	private String createUser;
	
	@Column(name="update_user")
	private String updateUser;
	
	@Column(name="default_pay_channel")
	private String defaultPayChannel;

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

	public String getDefaultPayChannel() {
		return defaultPayChannel;
	}

	public void setDefaultPayChannel(String defaultPayChannel) {
		this.defaultPayChannel = defaultPayChannel;
	}	
	
}
