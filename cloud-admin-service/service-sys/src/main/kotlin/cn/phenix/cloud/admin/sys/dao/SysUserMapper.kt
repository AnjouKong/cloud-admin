package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysUser
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author xiaobin
 * @create 2017-09-22 上午9:29
 */
interface SysUserMapper : GenericJpaRepository<SysUser, String> {

    /**
     * 根据登录名称查询用户
     * @return
     */
    fun findByLoginName(loginName: String): SysUser

    fun findByLoginNameAndDelFlag(loginName: String,delFlag: String): SysUser

    @Modifying
    @Query(value = "update SysUser set password = :newPasswordMD5 where id=:userId")
    fun doChangePassword(@Param("newPasswordMD5") newPasswordMD5: String,@Param("userId") userId: String)


}
