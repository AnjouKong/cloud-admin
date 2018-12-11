package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysUserMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.DataTableParameter
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.cloud.jpa.data.PageTable
import cn.phenix.model.sys.SysUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author xiaobin
 * @create 2017-09-22 上午9:30
 */
@Service
@Transactional(readOnly = true)
class SysUserService : CoreService<SysUserMapper, SysUser>() {

    @Autowired
    private val sysOfficeService: SysOfficeService? = null

    fun findByLoginName(loginName: String): SysUser {
        return dao.findByLoginName(loginName)
    }

    fun findByLoginNameAndDelFlag(loginName: String,delFlag: String): SysUser {
        return dao.findByLoginNameAndDelFlag(loginName,delFlag)
    }

    @Transactional
    fun delete(sysUser: SysUser) {
        dao.delete(sysUser)
    }

    @Transactional
    override fun save(user: SysUser): SysUser {
        val sysOffice = user.office
        val company = sysOfficeService!!.getCompanyByOfficeId(sysOffice.id)
        user.company = company
        return dao.save(user)
    }

    @Transactional
    fun update(user: SysUser) {
        val sysOffice = user.office
        val company = sysOfficeService!!.getCompanyByOfficeId(sysOffice.id)
        user.company = company
        dao.save(user)
    }

    @Transactional
    fun doChangePassword(newPasswordMD5: String, userId: String) {
        dao.doChangePassword(newPasswordMD5,userId)
    }

    fun findPage(officeId: String, loginname: String, username: String, parameter: DataTableParameter): PageTable<SysUser> {
        val pageRequest = parameter.pageRequest
        val finder = Finder.create("select user from SysUser user left join user.office office where user.delFlag=:delFlag")
                .setParam("delFlag", SysUser.DEL_FLAG_NORMAL)


        finder.search("office.id", officeId, Finder.SearchType.EQ).search("loginName", loginname, Finder.SearchType.EQ)
                .search("user.username", username, Finder.SearchType.EQ)

        val page = dao.find(finder, pageRequest)
        return PageTable(page)
    }

}
