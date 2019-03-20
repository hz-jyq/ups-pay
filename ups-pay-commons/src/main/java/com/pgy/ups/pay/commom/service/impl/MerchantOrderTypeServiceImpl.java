package com.pgy.ups.pay.commom.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pgy.ups.common.exception.BussinessException;
import com.pgy.ups.pay.commom.dao.MerchantOrderTypeDao;
import com.pgy.ups.pay.interfaces.cache.Cacheable;
import com.pgy.ups.pay.interfaces.entity.MerchantOrderTypeEntity;
import com.pgy.ups.pay.interfaces.service.config.MerchantOrderTypeService;

@Service
public class MerchantOrderTypeServiceImpl implements MerchantOrderTypeService,Cacheable<MerchantOrderTypeEntity>{
	
	
	@Resource
	private MerchantOrderTypeDao merchantOrderTypeDao;
    
	//查询商户支付产品对象
	@Override
	public MerchantOrderTypeEntity confirmMerchantOrderType(String fromSystem, String orderType) {
		
		Optional<MerchantOrderTypeEntity>  optional=merchantOrderTypeDao.confirmMerchantOrderType(fromSystem,orderType,new Date());
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new BussinessException("该商户不支持该支付产品，或有效日期已过期！");
		
	}

	@Override
	public String getCacheKeyname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, MerchantOrderTypeEntity> getCacheableData() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
