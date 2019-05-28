package com.pgy.ups.pay.interfaces.pay;

import com.pgy.ups.common.exception.BussinessException;
import com.pgy.ups.pay.interfaces.entity.UpsOrderEntity;
import com.pgy.ups.pay.interfaces.model.UpsResultModel;

public interface ProtocolCollectService {

    UpsResultModel protocolCollet(UpsOrderEntity orderEntity) throws BussinessException;

}
