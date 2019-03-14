package com.pgy.ups.pay.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pgy.ups.pay.interfaces.entity.MerchantOrderTypeEntity;
import com.pgy.ups.pay.interfaces.form.MerchantOrderTypeForm;

@Repository
public interface MerchantOrderTypeDubboDao extends JpaRepository<MerchantOrderTypeEntity, Long>{
    
	@Modifying
	@Transactional
	@Query(value="INSERT INTO ups_t_merchant_order_type VALUES(null,:#{#form.merchantId},:#{#form.orderTypeId},:#{#form.startTime},:#{#form.endTime)})",nativeQuery=true)
	void createMerchantOrderType(@Param("form")MerchantOrderTypeForm form);

}
