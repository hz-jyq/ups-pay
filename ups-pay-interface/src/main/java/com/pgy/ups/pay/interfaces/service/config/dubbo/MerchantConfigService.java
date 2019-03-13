package com.pgy.ups.pay.interfaces.service.config.dubbo;

import com.pgy.ups.common.page.PageInfo;
import com.pgy.ups.pay.interfaces.entity.MerchantConfigEntity;
import com.pgy.ups.pay.interfaces.form.MerchantConfigForm;

public interface MerchantConfigService {
	
	PageInfo<MerchantConfigEntity> queryByForm(MerchantConfigForm form);

	void enableMerchantConfig(Long id);
	
	void disableMerchantConfig(Long id);


}
