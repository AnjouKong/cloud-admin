package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.base.common.servlet.ValidateCodeServlet;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.security.Principal;
import cn.phenix.cloud.core.utils.CookieUtils;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.security.FormAuthenticationFilter;
import cn.phenix.cloud.security.SecurityUtils;
import cn.phenix.cloud.security.config.shiro.session.SessionDAO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static cn.phenix.cloud.security.SystemAuthorizingRealm.isValidateCodeLogin;

/**
 * 登录Controller
 */
@Controller
@Api(tags="系统登录")
public class LoginController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 管理登录
     */
    @GetMapping(value = "login")
    @ApiOperation("管理登录")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = SecurityUtils.getPrincipal();

        // 默认页签模式
        String tabmode = CookieUtils.getCookie(request, "tabmode");
        if (tabmode == null) {
            CookieUtils.setCookie(response, "tabmode", "1");
        }
        // 如果已经登录，则跳转到管理首页
        if (principal != null)
            return "redirect:/platform/home";
        return "login";
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @PostMapping(value = "login")
    @ApiOperation("登录失败")
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        Principal principal = SecurityUtils.getPrincipal();

        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            response.sendRedirect(":/platform/home");
            return null;
        }

        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
            message = "用户或密码错误, 请重试.";
        }

        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);


        if (logger.isDebugEnabled()) {
            logger.debug("login fail, active session size: {}, message: {}, exception: {}",
                    sessionDAO.getActiveSessions(false).size(), message, exception);
        }

        // 非授权异常，登录失败，验证码加1。
        if (!UnauthorizedException.class.getName().equals(exception)) {
            model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
        }

        // 验证失败清空验证码
        request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());

        //return Result.error(1, message);
        return "login";
    }

    /**
     * 登录成功，进入管理首页
     */
    @RequiresPermissions("user")
    @GetMapping(value = "")
    @ApiOperation("登录成功")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        Principal principal = SecurityUtils.getPrincipal();

        // 登录成功后，验证码计算器清零
        isValidateCodeLogin(principal.getLoginName(), false, true);

        if (logger.isDebugEnabled()) {
            logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }

        return "redirect:/platform/home";
    }

    /**
     * 获取主题方案
     */
    @GetMapping(value = "/theme/{theme}")
    @ApiOperation("获取主题")
    public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNotBlank(theme)) {
            CookieUtils.setCookie(response, "theme", theme);
        } else {
            theme = CookieUtils.getCookie(request, "theme");
        }
        return "redirect:" + request.getParameter("url");
    }
    @GetMapping(value = "logout")
    @ApiOperation(value = "退出登录")
    public void logout(HttpSession session) {
        try {
            Subject currentUser = org.apache.shiro.SecurityUtils.getSubject();
            currentUser.logout();
        } catch (SessionException ise) {

        } catch (Exception e) {

        }
    }
}
