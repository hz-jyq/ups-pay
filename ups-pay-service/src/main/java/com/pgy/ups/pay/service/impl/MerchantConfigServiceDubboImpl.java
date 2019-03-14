package com.pgy.ups.pay.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.alibaba.dubbo.config.annotation.Service;
import com.pgy.ups.common.page.PageInfo;
import com.pgy.ups.pay.interfaces.entity.MerchantConfigEntity;
import com.pgy.ups.pay.interfaces.form.MerchantConfigForm;
import com.pgy.ups.pay.interfaces.service.config.dubbo.MerchantConfigService;
import com.pgy.ups.pay.service.dao.MerchantConfigDao;

@Service
public class MerchantConfigServiceDubboImpl implements MerchantConfigService {

	private Logger logger = LoggerFactory.getLogger(MerchantConfigServiceDubboImpl.class);

	@Resource(name = "MerchantConfigDubboDao")
	private MerchantConfigDao merchantConfigDao;

	@Override
	public PageInfo<MerchantConfigEntity> queryByForm(MerchantConfigForm form) {
		Page<MerchantConfigEntity> page = merchantConfigDao.findByForm(form.getMerchantCode(), form.getMerchantName(),
				form.getDescription(), form.getPageRequest());
		return new PageInfo<>(page);
	}

	@Override
	public void enableMerchantConfig(Long id) {
		merchantConfigDao.enableOrDisableMerchantConfig(id, true);

	}

	@Override
	public void disableMerchantConfig(Long id) {
		merchantConfigDao.enableOrDisableMerchantConfig(id, false);

	}

	@Override
	public void deleteMerchantConfig(Long id) {
		merchantConfigDao.deleteById(id);
	}

	@Override
	public MerchantConfigEntity queryMerchantConfig(Long id) {
		return merchantConfigDao.findById(id).orElse(null);
	}

	@Override
	public boolean updateById(MerchantConfigForm form) {
		try {
			merchantConfigDao.updateById(form);
			return true;
		} catch (Exception e) {
			logger.error("修改商户配置异常：{}", e);
			return false;
		}
	}

	@Override
	public MerchantConfigEntity createmerchantConfig(MerchantConfigForm form) {
		try {
			MerchantConfigEntity mce = new MerchantConfigEntity();
			mce.setCreateTime(new Date());
			mce.setCreateUser(form.getCreateUser());
			mce.setDescription(form.getDescription());
			mce.setMerchantCode(form.getMerchantCode());
			mce.setMerchantName(form.getMerchantName());
			mce.setMerchantPublicKey(form.getMerchantPublicKey());
			mce.setUpdateTime(new Date());
			mce.setUpdateUser(form.getUpdateUser());
			mce.setUpsPrivateKey(form.getUpsPrivateKey());
			mce.setAvailable(false);
			return merchantConfigDao.saveAndFlush(mce);
		} catch (Exception e) {
			logger.error("新增商户配置异常：{}", e);
			return null;
		}

	}

}
