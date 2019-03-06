package com.pgy.ups.pay.route.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.pgy.ups.pay.commom.utils.CacheUtils;
import com.pgy.ups.pay.interfaces.cache.Cacheable;
import com.pgy.ups.pay.interfaces.entity.RouteMandatoryEntity;
import com.pgy.ups.pay.interfaces.service.route.RouteMandatoryService;
import com.pgy.ups.pay.route.dao.RouteMandatoryDao;

/**
 * 路由强制业务逻辑
 * 
 * @author 墨凉
 *
 */
@Service
public class RouteMandatoryServiceImpl implements RouteMandatoryService, Cacheable<RouteMandatoryEntity> {

	public static final String UPS_ROUTE_MANDATORY_CACHE = "ups-route-mandatory-cache";

	@Resource
	private RouteMandatoryDao routeMandatoryDao;

	@Resource
	private CacheUtils cacheUtils;

	@Override
	public RouteMandatoryEntity queryRouteMandatory(String fromSystem, String orderType) {
		//先从缓存中查找
		String key = CacheUtils.generateKey(fromSystem, orderType);
		RouteMandatoryEntity m = cacheUtils.getCacheByRediskeynameAndKey(UPS_ROUTE_MANDATORY_CACHE, key,
				RouteMandatoryEntity.class);
		return m;
	}

	@Override
	public String getCacheKeyname() {
		return UPS_ROUTE_MANDATORY_CACHE;
	}

	@Override
	public Map<String, RouteMandatoryEntity> getCacheableData() {
		RouteMandatoryEntity queryEntity = new RouteMandatoryEntity();
		queryEntity.setActive(true);
		List<RouteMandatoryEntity> list = routeMandatoryDao.findAll(Example.of(queryEntity));
		Map<String, RouteMandatoryEntity> cacheMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (RouteMandatoryEntity m : list) {
				cacheMap.put(CacheUtils.generateKey(m.getMerchantName(), m.getOrderType()), m);
			}
		}
		return cacheMap;
	}

}
