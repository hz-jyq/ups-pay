package com.pgy.ups.pay.commom.utils;

import java.io.Serializable;

import com.pgy.ups.pay.interfaces.enums.UpsResultEnum;
import com.pgy.ups.pay.interfaces.model.UpsResultModel;

public class UpsResultModelUtil {
	
	private UpsResultModelUtil() {}

    public static UpsResultModel upsResultModelSuccess(){
        return  new UpsResultModel(UpsResultEnum.SUCCESS);
    }

    public static UpsResultModel upsResultModelSuccess(Serializable result){
        return  new UpsResultModel(UpsResultEnum.SUCCESS,result);
    }
}
