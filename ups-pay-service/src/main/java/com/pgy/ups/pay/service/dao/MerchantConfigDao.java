package com.pgy.ups.pay.service.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pgy.ups.pay.interfaces.entity.MerchantConfigEntity;

@Repository("MerchantConfigDubboDao")
public interface MerchantConfigDao extends JpaRepository<MerchantConfigEntity,Long>{
    
	@Query(value = "SELECT * FROM ups_t_merchant_config c  WHERE IF (?1 != '',c.merchant_code =?1,1=1) AND IF (?2 !='',c.merchant_name LIKE %?2% ,1=1)  AND IF (?3 !='',c.merchant_name LIKE %?3% ,1=1) ORDER BY c.id DESC",  nativeQuery = true)
	Page<MerchantConfigEntity> findByForm( String merchantCode,String merchantName,String description,Pageable pageRequest);
    
	@Modifying
	@Query("UPDATE MerchantConfigEntity e SET e.available = true WHERE e.id=?1")
	@Transactional
	void enableMerchantConfig(Long id);
	
	@Modifying
	@Query("UPDATE MerchantConfigEntity e SET e.available = false WHERE e.id=?1")
	@Transactional
	void disableMerchantConfig(Long id);

}
