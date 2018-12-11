package cn.phenix.cloud.base.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaobin
 * @create 2017-09-25 下午6:52
 **/
public class WebInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            request.setAttribute("AppRoot", "");
            request.setAttribute("AppBase", "");
            request.setAttribute("AppName", "联合视界");
            request.setAttribute("AppDomain", "");
            request.setAttribute("AppShrotName", "联合视界");
        }
    }
}
