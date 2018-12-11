package cn.phenix.cloud.security;

import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.JsonUtils;
import cn.phenix.cloud.core.utils.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 表单验证（包含验证码）过滤类
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

    public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
    public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
    public static final String DEFAULT_MESSAGE_PARAM = "message";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
    private String messageParam = DEFAULT_MESSAGE_PARAM;

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        if (password == null) {
            password = "";
        }
        boolean rememberMe = isRememberMe(request);
        String host = Strings.getRemoteAddr((HttpServletRequest) request);
        String captcha = getCaptcha(request);
        boolean mobile = isMobileLogin(request);
        return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, mobile);
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    public String getMobileLoginParam() {
        return mobileLoginParam;
    }

    protected boolean isMobileLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getMobileLoginParam());
    }

    public String getMessageParam() {
        return messageParam;
    }

    /**
     * 登录成功之后跳转URL
     */
    @Override
    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request,
                                        ServletResponse response) throws Exception {
        //WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println(JsonUtils.writeObjectToJson(Result.success("success",getSuccessUrl())));
        } catch (IOException e1) {
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request, ServletResponse response) {
        if (!"XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request)
                .getHeader("X-Requested-With"))) {// 不是ajax请求
            setFailureAttribute(request, e);
            return true;
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
        }
        try {
            String message = e.getClass().getSimpleName();
            String className = e.getClass().getName();
//            Result result = Result.NEW();
            if (IncorrectCredentialsException.class.getName().equals(className)
                    || UnknownAccountException.class.getName().equals(className)) {
                message = "用户或密码错误, 请重试.";
                out.println(JsonUtils.writeObjectToJson(Result.error(3, message)));
            } else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
                message = StringUtils.replace(e.getMessage(), "msg:", "");
                out.println(JsonUtils.writeObjectToJson(Result.error(3, message)));
            } else {
                message = "系统出现点问题，请稍后再试！";
                e.printStackTrace(); // 输出到控制台
                out.println(JsonUtils.writeObjectToJson(Result.error(3, message)));
            }
            request.setAttribute(getFailureKeyAttribute(), className);
            request.setAttribute(getMessageParam(), message);
        } catch (Exception ex) {
            out.println(JsonUtils.writeObjectToJson(Result.error(3, ex.getMessage())));
        }finally {
            out.flush();
            out.close();
        }
        return false;
    }

}