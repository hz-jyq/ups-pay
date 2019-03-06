package com.pgy.ups.pay.route.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.pgy.ups.pay.commom.utils.CacheUtils;
import com.pgy.ups.pay.interfaces.cache.Cacheable;
import com.pgy.ups.pay.interfaces.entity.PayCompanyEntity;
import com.pgy.ups.pay.interfaces.entity.RouteMerchantChannelEntity;
import com.pgy.ups.pay.interfaces.service.route.PayCompanyService;
import com.pgy.ups.pay.interfaces.service.route.RouteMerchantChannelService;
import com.pgy.ups.pay.route.dao.PayCompanyDao;

@Service
public class PayCompanyServiceImpl implements PayCompanyService, Cacheable<PayCompanyEntity> {

	public static final String UPS_ROUTE_PAY_COMPANY_CACHE = "ups-route-pay-company-cache";

	@Resource
	private PayCompanyDao payCompanyDao;

	@Resource
	private RouteMerchantChannelService routeMerchantChannelService;

	@Resource
	private CacheUtils cacheUtils;

	@Override
	public List<PayCompanyEntity> queryAllAvailablePayChannels() {
		// 先查询缓存
		Map<String, PayCompanyEntity> cacheMap = cacheUtils.getCacheByRediskeyname(UPS_ROUTE_PAY_COMPANY_CACHE,
				PayCompanyEntity.class);
		List<PayCompanyEntity> list = new ArrayList<>(cacheMap.values());
		if (CollectionUtils.isEmpty(list)) {
			// 重新加载缓存
			cacheMap = getCacheableData();
			cacheUtils.setCacheByRediskeyname(UPS_ROUTE_PAY_COMPANY_CACHE, cacheMap);
			list.addAll(cacheMap.values());
		}
		return list;
	}
    
	/**
	 * 未配置的支付渠道要剔除
	 */
	@Override
	public List<PayCompanyEntity> queryMerchantAvailableChannels(String fromSystem, String orderType) {
		//商户配置的可支付渠道
		List<RouteMerchantChannelEntity> rmces = routeMerchantChannelService.queryMerchantAvailableChannels(fromSystem,
				orderType);
		//UPS所有的支付渠道
		List<PayCompanyEntity> list = queryAllAvailablePayChannels();
		Iterator<PayCompanyEntity> it=list.iterator();
		while(it.hasNext()) {
			PayCompanyEntity pce=it.next();
			if(!contains(rmces,pce.getCompanyCode())) {
				it.remove();
			}
		}
		return list;
	}

	private boolean contains(List<RouteMerchantChannelEntity> list, String companyCode) {
		if(CollectionUtils.isEmpty(list)) {
			return false;
		}
		for(RouteMerchantChannelEntity rmce:list) {
			if(StringUtils.equalsIgnoreCase(rmce.getPayChannel(), companyCode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getCacheKeyname() {

		return UPS_ROUTE_PAY_COMPANY_CACHE;
	}

	@Override
	public Map<String, PayCompanyEntity> getCacheableData() {
		List<PayCompanyEntity> list = payCompanyDao.queryAllAvailablePayChannels();
		Map<String, PayCompanyEntity> cacheMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (PayCompanyEntity pce : list) {
				cacheMap.put(pce.getCompanyCode(), pce);
			}
			return cacheMap;
		}
		return null;
	}

}
