package com.pgy.ups.pay.service.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pgy.ups.pay.interfaces.entity.MerchantConfigEntity;

@Repository("MerchantConfigDubboDao")
public interface MerchantConfigDao extends JpaRepository<MerchantConfigEntity,Long>{
    
	@Query(value = "SELECT * FROM ups_t_merchant_config c  WHERE IF (?1 != '',c.merchant_code =?1,1=1) AND IF (?2 !='',c.merchant_name LIKE %?2% ,1=1)  ORDER BY c.id ASC",  nativeQuery = true)
	Page<MerchantConfigEntity> findByForm( String merchantCode,String merchantName, Pageable pageRequest);

}
