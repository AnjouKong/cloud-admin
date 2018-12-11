package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysRole


/**
 * @author xiaobin
 * @create 2017-09-25 上午10:22
 */
interface SysRoleMapper : GenericJpaRepository<SysRole, String> {

    fun findByDelFlagAndName(delFlag: String, name: String): SysRole

    fun findByDelFlagAndEnname(delFlag: String, enname: String): SysRole

    fun findByDelFlagAndSysData(delFlag: String, sysData: String): SysRole
}
