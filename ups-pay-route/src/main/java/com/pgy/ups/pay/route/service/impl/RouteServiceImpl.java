package com.pgy.ups.pay.route.service.impl;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pgy.ups.common.exception.ParamValidException;
import com.pgy.ups.common.utils.SpringUtils;
import com.pgy.ups.pay.interfaces.entity.RouteMandatoryEntity;
import com.pgy.ups.pay.interfaces.model.UpsParamModel;
import com.pgy.ups.pay.interfaces.service.route.RouteMandatoryService;
import com.pgy.ups.pay.interfaces.service.route.RouteService;
import com.pgy.ups.pay.route.utils.RouteUtils;
import com.pgy.ups.pay.route.worker.impl.RouteChooseWorker;

@Service
public class RouteServiceImpl implements RouteService {

	private Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class);

	@Resource
	private RouteMandatoryService routeMandatoryService;

	/**
	 * 获取可用的路由信息
	 * 
	 * @return
	 * @throws ParamValidException
	 */
	@Override
	public String obtainAvalibaleRoute(UpsParamModel upsParamModel) {
		// 获取参数中自带的支付渠道
		String payChannel = upsParamModel.getPayChannel();
		if (StringUtils.isNoneBlank(payChannel)) {
			return payChannel;
		}

		// 若商户配置强制路由，则走强制路由
		RouteMandatoryEntity routeMandatoryEntity = routeMandatoryService
				.queryRouteMandatory(upsParamModel.getFromSystem(), upsParamModel.getOrderType());
		if (!Objects.isNull(routeMandatoryEntity)) {
			return routeMandatoryEntity.getPayChannel();
		}

		RouteChooseWorker routeChooseWorker = SpringUtils.getBean(RouteChooseWorker.class);
		try {
			routeChooseWorker.initRouteChooseHandler(upsParamModel);
			routeChooseWorker.doWorker();
			return routeChooseWorker.getWorkerResult();
		} catch (Exception e) {
			logger.error("路由选择发生异常“{}", ExceptionUtils.getStackTrace(e));
		}
		// 默认返回宝付
		return RouteUtils.getDefaultRouteResult(upsParamModel.getFromSystem());
	}

}
