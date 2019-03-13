package com.pgy.ups.pay.service.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;

import com.alibaba.dubbo.config.annotation.Service;
import com.pgy.ups.common.page.PageInfo;
import com.pgy.ups.pay.interfaces.entity.MerchantConfigEntity;
import com.pgy.ups.pay.interfaces.form.MerchantConfigForm;
import com.pgy.ups.pay.interfaces.service.config.dubbo.MerchantConfigService;
import com.pgy.ups.pay.service.dao.MerchantConfigDao;

@Service
public class MerchantConfigServiceDubboImpl implements MerchantConfigService {
    
	@Resource(name="MerchantConfigDubboDao")
	private MerchantConfigDao merchantConfigDao;

	@Override
	public PageInfo<MerchantConfigEntity> queryByForm(MerchantConfigForm form) {
		Page<MerchantConfigEntity> page = merchantConfigDao.findByForm( form.getMerchantCode(),form.getMerchantName(),
				form.getDescription(),form.getPageRequest());
		return new PageInfo<>(page);
	}


	@Override
	public void enableMerchantConfig(Long id) {
		merchantConfigDao.enableMerchantConfig(id);
		
	}

	@Override
	public void disableMerchantConfig(Long id) {
		merchantConfigDao.disableMerchantConfig(id);
		
	}

}
