package com.pgy.ups.pay.gateway.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.pgy.ups.common.utils.SpringUtils;
import com.pgy.ups.pay.interfaces.factory.BusinessFactory;
import com.pgy.ups.pay.interfaces.model.UpsParamModel;
import com.pgy.ups.pay.interfaces.model.UpsResultModel;
import com.pgy.ups.pay.interfaces.pay.BussinessHandler;

@Component
public class BussinessHandlerFactory<T> implements BusinessFactory<BussinessHandler<T, UpsResultModel>, UpsParamModel> {
	
	
	private Logger logger=LoggerFactory.getLogger(BussinessHandlerFactory.class);

	@Override
	public BussinessHandler<T, UpsResultModel> getInstance(UpsParamModel upm) {
		String payChannel = upm.getPayChannel();
		String orderType = upm.getOrderType();
		Class<?> interf=null;
		try {
			interf = Class.forName(BussinessHandler.class.getName());
		} catch (ClassNotFoundException e) {
			logger.error("反射获取业务处理接口失败！BussinessHandler",e);
			return null;
		}
		//dubbo硬编码获取服务
        ReferenceConfig<BussinessHandler<T, UpsResultModel>> rc=new ReferenceConfig<>();
        rc.setInterface(interf);
        rc.setGroup(payChannel + orderType);
        rc.setApplication(SpringUtils.getBean(ApplicationConfig.class));
        rc.setRegistry(SpringUtils.getBean(RegistryConfig.class));
        rc.setConsumer(SpringUtils.getBean(ConsumerConfig.class));
        BussinessHandler<T, UpsResultModel> h= rc.get();
        return h;

	}

}
