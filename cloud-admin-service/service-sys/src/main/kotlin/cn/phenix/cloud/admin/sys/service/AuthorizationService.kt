package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysUserMapper
import cn.phenix.cloud.core.model.BaseUser
import cn.phenix.cloud.core.security.IAuthorizationService
import cn.phenix.model.sys.SysUser
import com.google.common.collect.Lists
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author xiaobin
 * @create 2017-09-26 下午3:55
 */
@Service
class AuthorizationService : IAuthorizationService {


    @Autowired
    private val sysUserMapper: SysUserMapper? = null

    @Autowired
    private val sysMenuService: SysMenuService? = null


    /**
     * 根据登录名获取用户
     *
     * @param loginName
     * @return 取不到返回null
     */
    override fun getByLoginName(loginName: String): SysUser {
        return sysUserMapper!!.findByLoginNameAndDelFlag(loginName,"0")
    }

    override fun <T : BaseUser?> getPermissionList(baseUser: T): List<String> {
        val user = SysUser()
        user.id = baseUser!!.id
        val info = Lists.newArrayList<String>()
        val list = sysMenuService!!.findMenusByUser(user, null)
        list
                .filter { StringUtils.isNotBlank(it.permission) }
                .forEach { // 添加基于Permission的权限信息
                    info.addAll(Arrays.asList(*StringUtils.split(it.permission, ",")))
                }
        return info

    }
}
