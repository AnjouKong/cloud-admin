package cn.phenix.cloud.aop;

import cn.phenix.cloud.admin.sys.service.SysLogService;
import cn.phenix.cloud.core.security.Principal;
import cn.phenix.model.sys.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class WebLogAspect {
    protected static org.slf4j.Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
    @Resource
    private SysLogService sysLogService;
    @Pointcut("execution(* cn.phenix.cloud.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        try {
            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            //*========数据库日志=========*//
            SysLog log = new SysLog();

            Subject subject = org.apache.shiro.SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();

            if(principal==null){
                log.setUsername("用户登录");
            }else {
                log.setUsername(principal.getLoginName());
            }
            log.setType(request.getMethod());
            log.setUrl(request.getRequestURL().toString());
            log.setMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            log.setIp(request.getRemoteAddr());
            log.setArgs(Arrays.toString(joinPoint.getArgs()));
            Object target = joinPoint.getTarget();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod(); //获取被拦截的方法
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            Api api = target.getClass().getAnnotation(Api.class);
            if(api !=null){
                if(apiOperation !=null){
                    log.setTag(api.tags()[0]+"->"+apiOperation.value());
                }else{
                    log.setTag(api.tags()[0]);
                }
            }
            sysLogService.save(log);
        }  catch (Exception e) {
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }

    }

    @AfterThrowing(pointcut = "webLog()", throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e){
        try {
            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            //*========数据库日志=========*//
            SysLog log = new SysLog();

            Subject subject = org.apache.shiro.SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();

            if (principal == null) {
                log.setUsername("用户登录");
            } else {
                log.setUsername(principal.getLoginName());
            }
            log.setType(request.getMethod());
            log.setUrl(request.getRequestURL().toString());
            log.setMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            log.setIp(request.getRemoteAddr());
            log.setArgs(Arrays.toString(joinPoint.getArgs()));
            Object target = joinPoint.getTarget();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod(); //获取被拦截的方法
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            Api api = target.getClass().getAnnotation(Api.class);
            if (api != null) {
                if (apiOperation != null) {
                    log.setTag(api.tags()[0] + "->" + apiOperation.value());
                } else {
                    log.setTag(api.tags()[0]);
                }
            }
            sysLogService.save(log);
             /*========控制台输出=========*/
            logger.info("URL : " + request.getRequestURL().toString());
            logger.info("异常代码:" + e.getClass().getName());
            logger.info("异常信息:" + e.getMessage());
            logger.info("HTTP_METHOD : " + request.getMethod());
            logger.info("IP : " + request.getRemoteAddr());
            logger.info("异常方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        } catch (Exception ex) {
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
//        logger.error("异常方法:{}异常代码:{}异常信息:{}",joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage());
    }
}