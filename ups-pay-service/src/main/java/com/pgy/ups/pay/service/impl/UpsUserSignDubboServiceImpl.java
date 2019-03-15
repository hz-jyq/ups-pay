package com.pgy.ups.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pgy.ups.common.page.PageInfo;
import com.pgy.ups.pay.interfaces.entity.UpsAuthSignEntity;
import com.pgy.ups.pay.interfaces.entity.UpsUserSignLogEntity;
import com.pgy.ups.pay.interfaces.form.UpsUserSignForm;
import com.pgy.ups.pay.interfaces.service.auth.dubbo.UpsUserSignService;
import com.pgy.ups.pay.service.dao.UpsUserSignDubboDao;

import javax.annotation.Resource;

@Service
public class UpsUserSignDubboServiceImpl implements UpsUserSignService {

    @Resource
    private UpsUserSignDubboDao upsUserSignDubboDao;

    @Override
    public PageInfo<UpsAuthSignEntity> queryByForm(UpsUserSignForm upsBankForm) {
        return  new PageInfo<UpsAuthSignEntity>(upsUserSignDubboDao.queryByForm(upsBankForm));
    }
}
