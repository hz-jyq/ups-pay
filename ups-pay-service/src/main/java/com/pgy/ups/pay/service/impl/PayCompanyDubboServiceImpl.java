package com.pgy.ups.pay.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.pgy.ups.pay.interfaces.entity.PayCompanyEntity;
import com.pgy.ups.pay.interfaces.service.route.dubbo.PayCompanyService;
import com.pgy.ups.pay.service.dao.PayCompanyDubboDao;

@Service
public class PayCompanyDubboServiceImpl implements PayCompanyService{
	
	
	@Resource
	private PayCompanyDubboDao payCompanyDubboDao;

	@Override
	public List<PayCompanyEntity> queryAllAvailablePayChannels() {
		
		return payCompanyDubboDao.findAll();
	}

}
