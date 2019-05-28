package com.pgy.ups.pay.gateway.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    // 每秒发放许可数
    double permitsPerSecond() default 50;
    // 超时时间，即能否在指定内得到令牌，如果不能则立即返回，不进入目标方法/类
    // 默认为0，即不等待，获取不到令牌立即返回
    long timeout() default 700000;
    // 超时时间单位，默认取毫秒
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;


}
