package com.pgy.ups.pay.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fescar.core.context.RootContext;
import com.alibaba.fescar.spring.annotation.GlobalTransactional;
import com.pgy.ups.common.page.PageInfo;
import com.pgy.ups.pay.interfaces.entity.UpsAuthSignEntity;
import com.pgy.ups.pay.interfaces.form.UpsUserSignForm;
import com.pgy.ups.pay.interfaces.service.auth.dubbo.UpsUserSignService;
import com.pgy.ups.pay.interfaces.service.balance.UpsBalanceTransferService;
import com.pgy.ups.pay.service.dao.UpsUserSignDubboDao;


@Service
public class UpsUserSignDubboServiceImpl implements UpsUserSignService {

    @Resource
    private UpsUserSignDubboDao upsUserSignDubboDao;

    @Reference
    private UpsBalanceTransferService upsBalanceTransferService;


    @Override
    @GlobalTransactional(timeoutMills = 300, name = "dubbo-gts-fescar-example")
    public PageInfo<UpsAuthSignEntity> queryByForm(UpsUserSignForm upsBankForm) {
        System.out.println("全局事务id ：" + RootContext.getXID());
        upsBalanceTransferService.balanceTransferQuartz();
        return  new PageInfo<>(upsUserSignDubboDao.queryByForm(upsBankForm,upsBankForm.getPageRequest()));
    }
}
