package com.pgy.ups.pay.route.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.pgy.ups.common.utils.SpringUtils;
import com.pgy.ups.pay.commom.service.impl.MerchantConfigServiceImpl;
import com.pgy.ups.pay.commom.utils.CacheUtils;
import com.pgy.ups.pay.interfaces.entity.MerchantConfigEntity;
import com.pgy.ups.pay.interfaces.entity.PayCompanyEntity;
import com.pgy.ups.pay.interfaces.service.config.MerchantConfigService;

@Component
public class RouteUtils {

	private static Logger logger = LoggerFactory.getLogger(RouteUtils.class);

	public static final String BAO_FOO = "baofoo";

	public static final String YEE_PAY = "yeepay";

	@Resource
	private MerchantConfigService merchantConfigService;

	/**
	 * 获取默认的路由结果
	 * 
	 * @return
	 */
	public static String getDefaultRouteResult(String fromSystem) {

		CacheUtils cacheUtils = SpringUtils.getBean(CacheUtils.class);
		// 从缓存中获取商户配置
		MerchantConfigEntity entity = cacheUtils.getCacheByRediskeynameAndKey(
				MerchantConfigServiceImpl.UPS_MERCHANT_CACHE, fromSystem, MerchantConfigEntity.class);
		if (Objects.isNull(entity) || StringUtils.isEmpty(entity.getDefaultPayChannel())) {
			// 缓存中没有则查询数据库
			List<MerchantConfigEntity> list = SpringUtils.getBean(MerchantConfigService.class)
					.queryAvaliableMerchantList();
			if (CollectionUtils.isEmpty(list)) {
				logger.warn("没有查询到默认的路由信息，list=null!");
				return null;
			}
			// list转为map缓存并放入redis中
			Map<String, MerchantConfigEntity> cacheMap = new HashMap<>();
			for (MerchantConfigEntity mce : list) {
				cacheMap.put(mce.getMerchantName(), mce);
			}
			cacheUtils.setCacheByRediskeyname(MerchantConfigServiceImpl.UPS_MERCHANT_CACHE, cacheMap);
			entity = cacheMap.get(fromSystem);
			if (Objects.isNull(entity) || StringUtils.isEmpty(entity.getDefaultPayChannel())) {
				logger.warn("没有查询到默认的路由信息,fromSystem:{},MerchantConfigEntity:{}", fromSystem, entity);
				return null;
			}
		}
		return entity.getDefaultPayChannel();
	}

	/**
	 * 根据渠道列表选择最好的支付渠道 
	 * 
	 * @param payCannels
	 * @return
	 */
	public static String getPerfectRouteResult(List<PayCompanyEntity> payChannels) {
		//获取商户默认渠道		
		return payChannels.get(0).getCompanyCode();
	}

}
