package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysRoleMapper
import cn.phenix.cloud.core.config.Global
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.DataTableParameter
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.cloud.jpa.data.PageTable
import cn.phenix.cloud.jpa.dynamic.Parameter
import cn.phenix.model.sys.SysMenu
import cn.phenix.model.sys.SysRole
import cn.phenix.model.sys.SysUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author xiaobin
 * @create 2017-10-08 上午9:12
 */
@Service
@Transactional(readOnly = true)
class SysRoleService : CoreService<SysRoleMapper, SysRole>() {

    val roleBySys: SysRole
        get() = dao.findByDelFlagAndSysData(SysRole.DEL_FLAG_NORMAL, Global.YES)

    fun findPage(officeId: String, parameter: DataTableParameter): PageTable<SysRole> {
        val pageRequest = parameter.pageRequest
        val finder = Finder.create("select role from SysRole role left join role.office office")
        finder.whereExcludeDel("role")
        finder.search("office.id", officeId)
        return PageTable(dao.find(finder, pageRequest))
    }

    fun findByRoleName(roleName: String): SysRole {
        return dao.findByDelFlagAndName(SysRole.DEL_FLAG_NORMAL, roleName)
    }

    fun findByRoleEnname(enname: String): SysRole {
        return dao.findByDelFlagAndEnname(SysRole.DEL_FLAG_NORMAL, enname)
    }

    @Transactional
    fun updateMenuRole(menuIdList: Array<String>, roleId: String): SysRole {
        val role = get(roleId)
        role.menuList.clear()
        for (menuId in menuIdList) {
            role.menuList.add(SysMenu(menuId))
        }
        saveRole(role)
        return role
    }

    @Transactional
    fun pushUser(userIds: Array<String>, roleId: String) {
        for (userId in userIds) {
            updateByNamedQuery(Parameter.create("insertUserRole").insert("roleId", roleId).insert("userId", userId))
        }
    }

    @Transactional
    fun deleteUser(userIds: Array<String>, roleId: String) {
        for (userId in userIds) {
            updateByNamedQuery(Parameter.create("deleteUserRole").insert("roleId", roleId).insert("userId", userId))
        }
    }

    @Transactional
    fun saveRole(sysRole: SysRole) {
        dao.save(sysRole)
    }

    @Transactional
    fun delete(ids: Array<String>) {
        for (id in ids) {
            dao.updateDelFlag(id)
        }
    }
}
