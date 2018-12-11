package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysDictMapper
import cn.phenix.cloud.admin.sys.dao.SysOfficeMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.model.sys.SysDict
import cn.phenix.model.sys.SysMenu
import cn.phenix.model.sys.SysOffice
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.ArrayList
import java.util.Date


/**
 * 组织架构管理
 *
 * @author xiaobin
 * @create 2017-09-26 下午2:55
 */
@Service
@Transactional(readOnly = true)
class SysOfficeService: CoreService<SysOfficeMapper, SysOffice>()  {

    @Autowired
    private val sysOfficeMapper: SysOfficeMapper? = null

    @Autowired
    private val sysUserService: SysUserService? = null

    /*operator fun get(id: String): SysOffice? {
        return if (StringUtils.isBlank(id)) null else sysOfficeMapper!!.findById(id).orElse(null)
    }*/

    fun getCompanyByOfficeId(officeId: String): SysOffice? {
        val office = get(officeId) ?: return null
        return if ("1" == office.type) {
            office
        } else {
            getCompanyByOfficeId(office.parent.id)
        }
    }

    fun findOfficeListByParentId(parentId: String, isAdmin: Boolean): List<SysOffice> {
        val officeList: List<SysOffice>
        val finder = Finder.create("select o from SysOffice o where o.delFlag=:delFlag")
                .setParam("delFlag", SysOffice.DEL_FLAG_NORMAL)
        finder.search("o.parent.id", parentId, Finder.SearchType.EQ)
        finder.append("order by o.sort")
        officeList = if (isAdmin) {
            sysOfficeMapper!!.find(finder)
        } else {
            //todo:權限過濾
            //office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
            sysOfficeMapper!!.find(finder)
        }
        return officeList
    }

    @Transactional(readOnly = true)
    fun findList(office: SysOffice?): List<SysOffice> {
        return if (office != null) {
            sysOfficeMapper!!.findByDelFlagAndParentIdsLike(SysMenu.DEL_FLAG_NORMAL, office.parentIds + "%")
        } else ArrayList()
    }

    /*@Transactional
    fun save(office: SysOffice) {
        office.updateDate = Date()
        sysOfficeMapper!!.save(office)
    }

    @Transactional
    fun deleteById(id: String) {
        sysOfficeMapper!!.deleteById(id)
    }
*/
}
