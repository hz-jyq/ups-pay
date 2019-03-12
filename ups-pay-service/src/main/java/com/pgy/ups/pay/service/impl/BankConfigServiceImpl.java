package com.pgy.ups.pay.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;

import com.alibaba.dubbo.config.annotation.Service;
import com.pgy.ups.common.page.PageInfo;
import com.pgy.ups.pay.interfaces.entity.UpsBankEntity;
import com.pgy.ups.pay.interfaces.form.UpsBankForm;
import com.pgy.ups.pay.interfaces.service.config.dubbo.BankConfigService;
import com.pgy.ups.pay.service.dao.BankConfigDao;

/**
 * Hello world!
 *
 */
@Service
public class BankConfigServiceImpl implements BankConfigService {

	@Resource
	private BankConfigDao bankConfigDao;

	@Override
	public PageInfo<UpsBankEntity> queryAll(UpsBankForm upsBankForm) {

		Page<UpsBankEntity> page = bankConfigDao.findByForm(upsBankForm.getBankCode(), upsBankForm.getBankName(),
				upsBankForm.getPageRequest());
		return new PageInfo<>(page);
	}

	@Override
	public void deleteBankConfigById(Long id) {
		bankConfigDao.deleteById(id);
	}

	@Override
	public UpsBankEntity saveBankConfig(UpsBankForm form) {
		UpsBankEntity entity = new UpsBankEntity();
		entity.setBankCode(form.getBankCode());
		entity.setBankName(form.getBankName());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		entity.setCreateUser(form.getCreateUser());
		entity.setUpdateUser(form.getUpdateUser());
		return bankConfigDao.save(entity);
	}

}