package cn.phenix.cloud.security;

import cn.phenix.cloud.core.security.Principal;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @author xiaobin
 * @create 2017-09-25 上午10:10
 **/
public class SecurityUtils {


    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return org.apache.shiro.SecurityUtils.getSubject();
    }


    /**
     * 获取当前登录者对象
     */
    public static Principal getPrincipal() {
        try {
            Subject subject = org.apache.shiro.SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
        } catch (UnavailableSecurityManagerException | InvalidSessionException e) {

        }
        return null;
    }

    public static Session getSession() {
        try {
            Subject subject = org.apache.shiro.SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
//			subject.logout();
        } catch (InvalidSessionException e) {

        }
        return null;
    }

   /* public static SysUser getUser() {
        SystemAuthorizingRealm.Principal principal = getPrincipal();
        if (principal != null) {
            SysUserService sysUserService = SpringContextHolder.getBean(SysUserService.class);
            SysUser user = sysUserService.get(principal.getId());
            if (user != null) {
                return user;
            }
            return new SysUser();
        }
        // 如果没有登录，则返回实例化空的User对象。
        return new SysUser();
    }*/
}
