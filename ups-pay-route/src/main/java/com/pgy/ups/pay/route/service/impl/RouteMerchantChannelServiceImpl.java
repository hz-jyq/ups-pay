package com.pgy.ups.pay.route.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.pgy.ups.pay.commom.utils.CacheUtils;
import com.pgy.ups.pay.interfaces.cache.Cacheable;
import com.pgy.ups.pay.interfaces.entity.MerchantConfigEntity;
import com.pgy.ups.pay.interfaces.entity.RouteMerchantChannelEntity;
import com.pgy.ups.pay.interfaces.entity.UpsOrderTypeEntity;
import com.pgy.ups.pay.interfaces.service.config.MerchantConfigService;
import com.pgy.ups.pay.interfaces.service.order.UpsOrderTypeService;
import com.pgy.ups.pay.interfaces.service.route.RouteMerchantChannelService;
import com.pgy.ups.pay.route.dao.RouteMerchantChannelDao;

/**
 * 路由时，商户可选的渠道列表
 * 
 * @author 墨凉
 *
 */
@Service
public class RouteMerchantChannelServiceImpl
		implements RouteMerchantChannelService, Cacheable<ArrayList<RouteMerchantChannelEntity>> {

	public static final String UPS_ROUTE_MERCHANT_AVAILABLE_CHANNELS_CACHE = "ups-route-merchant-available-channels-cache";

	@Resource
	private MerchantConfigService merchantConfigService;

	@Resource
	private UpsOrderTypeService upsOrderTypeService;

	@Resource
	private CacheUtils cacheUtils;

	@Resource
	private RouteMerchantChannelDao routeMerchantChannelDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<RouteMerchantChannelEntity> queryMerchantAvailableChannels(String fromSystem, String orderType) {
		String key = CacheUtils.generateKey(fromSystem, orderType);
		ArrayList<RouteMerchantChannelEntity> list=new ArrayList<>();
		list.addAll(cacheUtils.getCacheByRediskeynameAndKey(UPS_ROUTE_MERCHANT_AVAILABLE_CHANNELS_CACHE, key,list.getClass()
				));
		if(CollectionUtils.isEmpty(list)) {
			RouteMerchantChannelEntity queryEntity = new RouteMerchantChannelEntity();
			queryEntity.setMerchantName(fromSystem);
			queryEntity.setOrderType(orderType);
			queryEntity.setActive(true);
			list.addAll(routeMerchantChannelDao.findAll(Example.of(queryEntity))) ;
			cacheUtils.setCacheByRediskeynameAndKey(UPS_ROUTE_MERCHANT_AVAILABLE_CHANNELS_CACHE, key, list);
		}
        return list;
	}

	@Override
	public String getCacheKeyname() {
		return UPS_ROUTE_MERCHANT_AVAILABLE_CHANNELS_CACHE;
	}

	@Override
	public Map<String, ArrayList<RouteMerchantChannelEntity>> getCacheableData() {
		List<UpsOrderTypeEntity> orderTypes = upsOrderTypeService.getAllOrderType();
		List<MerchantConfigEntity> merchantConfigs = merchantConfigService.queryAvaliableMerchantList();
		Map<String, ArrayList<RouteMerchantChannelEntity>> cacheMap = new HashMap<>();
		for (MerchantConfigEntity mce : merchantConfigs) {
			for (UpsOrderTypeEntity orderType : orderTypes) {
				RouteMerchantChannelEntity queryEntity = new RouteMerchantChannelEntity();
				queryEntity.setMerchantName(mce.getMerchantName());
				queryEntity.setOrderType(orderType.getOrderType());
				queryEntity.setActive(true);
				List<RouteMerchantChannelEntity> list = routeMerchantChannelDao.findAll(Example.of(queryEntity));
				cacheMap.put(CacheUtils.generateKey(mce.getMerchantName(), orderType.getOrderType()),
						new ArrayList<>(list));
			}
		}
		return cacheMap;
	}

}
