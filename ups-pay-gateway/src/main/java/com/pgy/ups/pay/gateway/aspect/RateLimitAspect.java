package com.pgy.ups.pay.gateway.aspect;

import com.google.common.util.concurrent.RateLimiter;
import com.pgy.ups.common.exception.BussinessException;
import com.pgy.ups.pay.gateway.annotation.RateLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Scope
@Aspect
public class RateLimitAspect  {

    //用来存放不同接口的RateLimiter(key为接口名称，value为RateLimiter)
    private ConcurrentHashMap<String, RateLimiter> map = new ConcurrentHashMap<>();

    private RateLimiter rateLimiter;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.pgy.ups.pay.gateway.annotation.RateLimit)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Object obj = null;
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = joinPoint.getTarget();
        //为了获取注解信息
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解信息
        RateLimit annotation = currentMethod.getAnnotation(RateLimit.class);
        double limitNum = annotation.permitsPerSecond(); //获取注解每秒加入桶中的token
        long timeout = annotation.timeout();
        TimeUnit timeUnit = annotation.timeUnit();

        String functionName = msig.getName(); // 注解所在方法名区分不同的限流策略

        //获取rateLimiter
        if(map.containsKey(functionName)){
            rateLimiter = map.get(functionName);
        }else {
            map.put(functionName, RateLimiter.create(limitNum));
            rateLimiter = map.get(functionName);
        }
        try {
            if (rateLimiter.tryAcquire(timeout, timeUnit)) {
                //执行方法
                obj = joinPoint.proceed();
            } else {
                logger.info("拒绝了请求");
                throw new BussinessException("请求拒绝！超过最大限制");
            }
        } catch (Throwable throwable) {
            throw new BussinessException("服务异常");
        }
        return obj;
    }



}
