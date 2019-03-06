package com.pgy.ups.pay.interfaces.service.route;

import com.pgy.ups.pay.interfaces.entity.RouteMandatoryEntity;

/**
 * 路由强制业务逻辑
 * @author 墨凉
 *
 */
public interface RouteMandatoryService {
    
	/**
	 * 通过商户信息和订单类型查询路由强制信息
	 * @param fromSystem
	 * @param orderType
	 * @return
	 */
	RouteMandatoryEntity queryRouteMandatory(String fromSystem, String orderType);

}
