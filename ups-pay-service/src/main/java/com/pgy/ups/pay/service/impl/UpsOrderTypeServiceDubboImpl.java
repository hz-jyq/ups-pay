package com.pgy.ups.pay.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.pgy.ups.pay.interfaces.entity.UpsOrderTypeEntity;
import com.pgy.ups.pay.interfaces.service.order.dubbo.UpsOrderTypeService;
import com.pgy.ups.pay.service.dao.OrderTypeDubboDao;

@Service
public class UpsOrderTypeServiceDubboImpl implements UpsOrderTypeService{
	
	@Resource
	private OrderTypeDubboDao orderTypeDubboDao;

	@Override
	public List<UpsOrderTypeEntity> getAllOrderType() {
		return orderTypeDubboDao.findAll();
	}
	
	

}
