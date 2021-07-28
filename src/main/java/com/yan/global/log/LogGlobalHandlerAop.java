package com.yan.global.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志的全局处理切面
 * 正常日志写入log/normal/xxx.log
 * 异常日志写入log/error/xxx.log
 * log的msg格式为：访问者IP^访问的URL^参数^访问时间ms/异常的msg^结果
 *
 * @author : 鄢云峰
 */
@Aspect
@Component
@Slf4j
public class LogGlobalHandlerAop {

    private static final String LOG_SEPARATOR = "^";

    @Pointcut("execution(* com.yan.controller.*.*(..))")
    public void logAop() {
    }

    @Around("logAop()")
    public Object normalLog2File(ProceedingJoinPoint joinPoint) throws Throwable {
        String[] logs = new String[5];
        logBuffer(joinPoint, logs);
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - start;
        logs[3] = String.valueOf(time);
        log.info(String.join(LOG_SEPARATOR, logs));
        return result;
    }

    @AfterThrowing(pointcut = "logAop()", throwing = "throwable")
    public void errorLog2File(JoinPoint joinPoint, Throwable throwable) {
        String[] logs = new String[3];
        logBuffer(joinPoint, logs);
        log.error(String.join(LOG_SEPARATOR, logs), throwable);
    }

    private void logBuffer(JoinPoint joinPoint, String[] logs) {
        Object[] args = joinPoint.getArgs();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new NullPointerException(joinPoint.getSignature().getName() + "中的ServletRequestAttributes为null");
        }
        HttpServletRequest request = attributes.getRequest();
        request.getRemoteAddr();
        logs[0] = request.getRemoteAddr();
        logs[1] = request.getRequestURL().toString();
        logs[2] = args[0].toString();
    }

}
