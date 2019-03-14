package com.pgy.ups.pay.interfaces.service.order.dubbo;

import com.pgy.ups.common.page.PageInfo;
import com.pgy.ups.pay.interfaces.entity.OrderPushEntity;
import com.pgy.ups.pay.interfaces.entity.UpsOrderEntity;
import com.pgy.ups.pay.interfaces.form.UpsOrderForm;
import com.pgy.ups.pay.interfaces.model.UpsParamModel;

/**
 * Ups订单业务逻辑
 * @author 墨凉
 *
 */
public interface UpsOrderService {

	//查询订单
	PageInfo<UpsOrderEntity> getUpsOrder(UpsOrderForm form);
}
