package com.pgy.ups.pay.interfaces.service.product;

import com.pgy.ups.pay.interfaces.entity.UpsMerchantTransitionEntity;
import com.pgy.ups.pay.interfaces.entity.UpsProductEntity;

public interface UpsProductService {

    UpsProductEntity getEnableProduct(Long productId);

    void saveTransitionEntity(UpsMerchantTransitionEntity upsMerchantTransitionEntity);

}


