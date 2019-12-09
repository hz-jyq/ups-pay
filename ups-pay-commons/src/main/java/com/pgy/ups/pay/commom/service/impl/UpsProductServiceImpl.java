package com.pgy.ups.pay.commom.service.impl;


import com.pgy.ups.pay.commom.dao.UpsMerchantDao;
import com.pgy.ups.pay.commom.dao.UpsProductDao;
import com.pgy.ups.pay.interfaces.entity.UpsMerchantEntity;
import com.pgy.ups.pay.interfaces.entity.UpsMerchantTransitionEntity;
import com.pgy.ups.pay.interfaces.entity.UpsProductEntity;
import com.pgy.ups.pay.interfaces.entity.UpsProductTransitionEntity;
import com.pgy.ups.pay.interfaces.service.product.UpsProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UpsProductServiceImpl implements UpsProductService {

    @Resource
    private UpsProductDao upsProductDao;

    @Resource
    private UpsMerchantDao upsMerchantDao;

    @Override
    public UpsProductEntity getEnableProduct(Long productId){
        return  upsProductDao.getEnableProduct(productId);
    }

    @Override
    public void saveTransitionEntity(UpsMerchantTransitionEntity upsMerchantTransitionEntity) {
        if(upsMerchantTransitionEntity == null || upsMerchantTransitionEntity.getMerchant_id() == null ){
            return;
        }
        UpsMerchantEntity upsMerchantEntity =   new UpsMerchantEntity(upsMerchantTransitionEntity);
        upsMerchantDao.saveAndFlush(upsMerchantEntity);
        for(UpsProductTransitionEntity entity : upsMerchantTransitionEntity.getProduct_list()){
            entity.setMerchant_id(upsMerchantEntity.getId());
            UpsProductEntity upsProductEntity = new UpsProductEntity(entity);
               upsProductDao.saveAndFlush(upsProductEntity);
         }
    }


}
