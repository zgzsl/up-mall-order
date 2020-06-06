package com.zsl.upmall.component;

import com.zsl.upmall.exception.RequestLimitException;
import com.zsl.upmall.service.RedisService;
import com.zsl.upmall.util.IpUtil;
import com.zsl.upmall.validator.RequestLimit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Order(3)
public class RequestLimitAspect {

    @Autowired
    private RedisService redisService;

    @Before("@annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint , RequestLimit limit) throws Exception {
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = IpUtil.getRequestIp(request);
        boolean isLimit = testLogin(ip,limit.time(),limit.count(), request.getSession().getId());
        if(!isLimit){
            throw new RequestLimitException("不要频繁操作,60秒后再试");
        }
    }

    /**
     * 模拟限制ip指定时间段内访问次数
     * @param ip
     * @return
     */
    public boolean testLogin(String ip,Long time,Integer count,String session) {
        String value = redisService.get(session+ip);
        if(value==null){
            redisService.set(session+ip, "1");
            redisService.expire(session+ip, time);
            return true;
        }else{
            int parseInt = Integer.parseInt(value);
            if(parseInt >= count){
                System.out.println("访问受限！！！！");
                return false;
            }
            redisService.increment(session+ip,1);
        }
        return true;
    }
}