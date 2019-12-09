package com.pgy.ups.pay.commom.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgy.ups.pay.interfaces.entity.UpsThirdpartyConfigEntity;

@Repository
public interface UpsThirdpartyConfigDao extends JpaRepository<UpsThirdpartyConfigEntity, Long>{
    

	UpsThirdpartyConfigEntity queryByProductIdAndPayChannelAndOrderType(Long productId,String payChannel, String orderType);

}
