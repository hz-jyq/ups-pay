package com.pgy.ups.pay.interfaces.pay;

import com.pgy.ups.common.exception.BussinessException;

public interface BussinessHandler<T,R> {
	
	   R handler(T t) throws BussinessException;

}
