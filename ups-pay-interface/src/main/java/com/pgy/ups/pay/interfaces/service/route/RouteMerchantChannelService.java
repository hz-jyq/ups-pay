package com.pgy.ups.pay.interfaces.service.route;

import java.util.List;

import com.pgy.ups.pay.interfaces.entity.RouteMerchantChannelEntity;

/**
 * 路由时，商户可选的渠道列表
 * @author 墨凉
 *
 */
public interface RouteMerchantChannelService {

	List<RouteMerchantChannelEntity> queryMerchantAvailableChannels(String fromSystem, String orderType);
	
}
