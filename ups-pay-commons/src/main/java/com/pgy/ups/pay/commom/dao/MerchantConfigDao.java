package com.pgy.ups.pay.commom.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pgy.ups.pay.interfaces.entity.MerchantConfigEntity;

@Repository
public interface MerchantConfigDao extends JpaRepository<MerchantConfigEntity, Long> {
    
    
	/**
	 * 查询商户配置
	 * @param fromSystem available date
	 * @return
	 */
	@Query(value="select c from MerchantConfigEntity c where c.merchantName=?1 and c.available =?2 and (?3 between c.startTime and c.endTime)")
	MerchantConfigEntity queryByMerchant(String fromSystem, boolean available, Date date);
	
    
	/**
	 * 
	 * @param 查询所有可用的商户配置
	 * @param available date
	 * @return
	 */
	@Query(value="select c from MerchantConfigEntity c where c.available =?1 and (?2 between c.startTime and c.endTime)")
	List<MerchantConfigEntity> querByAvaliableMerchantList(boolean available, Date date);

}
