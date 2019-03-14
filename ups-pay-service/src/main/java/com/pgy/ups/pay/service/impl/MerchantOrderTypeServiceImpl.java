package com.pgy.ups.pay.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.pgy.ups.pay.interfaces.form.MerchantOrderTypeForm;
import com.pgy.ups.pay.interfaces.service.config.dubbo.MerchantOrderTypeService;
import com.pgy.ups.pay.service.dao.MerchantOrderTypeDubboDao;

@Service
public class MerchantOrderTypeServiceImpl implements MerchantOrderTypeService{
	
	@Resource
	private MerchantOrderTypeDubboDao merchantOrderTypeDubboDao;

	@Override
	public void createMerchantOrderType(MerchantOrderTypeForm form) {
		
		merchantOrderTypeDubboDao.createMerchantOrderType(form);
		
	}

}
