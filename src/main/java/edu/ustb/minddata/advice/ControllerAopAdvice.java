package edu.ustb.minddata.advice;

import cn.hutool.core.util.StrUtil;
import edu.ustb.minddata.utils.LogUtil;
import edu.ustb.minddata.config.datasource.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author UmiSkky
 */
@Aspect
@Component
@Slf4j(topic = "interface")
public class ControllerAopAdvice {

    @Pointcut("execution(public edu.ustb.minddata.exception.ResultBody edu.ustb.minddata.controller.*.*(..))")
    public void allController(){}

    /**
     * 添加接口访问日志输出
     */
    @Before("allController()")
    public void printUrlLog(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes != null){
            HttpServletRequest request = servletRequestAttributes.getRequest();
            log.info(LogUtil.interfaceLog(request));
        }
    }

    /**
     * 数据库切换
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return Object
     * @throws Throwable 异常
     */
    @Around("@annotation(edu.ustb.minddata.config.datasource.SwitchDataSource)")
    public Object switchDataSource(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Map<String, Object> fieldsName = getFieldsName(proceedingJoinPoint);
        String rid = (String) fieldsName.get("rid");
        Object proceed = null;
        if(!StrUtil.isEmpty(rid)){
            DynamicDataSourceContextHolder.setDataSourceKey(rid);
            proceed = proceedingJoinPoint.proceed();
            DynamicDataSourceContextHolder.clearDataSourceKey();
        }
        return proceed;
    }

    /**
     * 出现异常切回默认数据库
     */
    @AfterThrowing("@annotation(edu.ustb.minddata.config.datasource.SwitchDataSource)")
    public void switchDataSourceError(){
        DynamicDataSourceContextHolder.clearDataSourceKey();
    }

    /**
     * 获得切入点方法参数Map
     * @param joinPoint ProceedingJoinPoint
     * @return Map<String, Object>
     */
    private static Map<String, Object> getFieldsName(ProceedingJoinPoint joinPoint) {
        // 参数值
        Object[] args = joinPoint.getArgs();
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = pnd.getParameterNames(method);
        Map<String, Object> paramMap = new HashMap<>(32);
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                paramMap.put(parameterNames[i], args[i]);
            }
        }
        return paramMap;
    }
}
