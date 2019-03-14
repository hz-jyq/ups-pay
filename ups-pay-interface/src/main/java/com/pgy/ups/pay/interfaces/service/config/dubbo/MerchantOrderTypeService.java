package com.pgy.ups.pay.interfaces.service.config.dubbo;

import com.pgy.ups.pay.interfaces.form.MerchantOrderTypeForm;

public interface MerchantOrderTypeService {

	boolean createMerchantOrderType(MerchantOrderTypeForm form);

	void deleteMerchantOrderType(Long id);

	boolean updateMerchantOrderType(MerchantOrderTypeForm form);

}
