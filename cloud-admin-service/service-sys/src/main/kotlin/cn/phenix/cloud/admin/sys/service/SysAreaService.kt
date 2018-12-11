package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysAreaMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.model.sys.SysArea
import cn.phenix.model.sys.SysOffice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 地区管理
 *
 */
@Service
@Transactional(readOnly = true)
class SysAreaService : CoreService<SysAreaMapper, SysArea>()  {

    @Autowired
    private val sysAreaMapper: SysAreaMapper? = null

    fun findAreaListByParentId(parentId: String): List<SysArea> {

        val officeList: List<SysArea>
        val finder = Finder.create("select o from SysArea o where o.delFlag=:delFlag")
                .setParam("delFlag", SysOffice.DEL_FLAG_NORMAL)
        finder.search("o.parent.id", parentId, Finder.SearchType.EQ)
        finder.append("order by o.sort")
        officeList = sysAreaMapper!!.find(finder)
        return officeList
    }
    fun findProvenceByPid(parentId: String): List<SysArea> {

        val officeList: List<SysArea>
        val finder = Finder.create("select o from SysArea o where o.delFlag=:delFlag and type <= 3 and name !='市辖区'")//省市
                .setParam("delFlag", SysOffice.DEL_FLAG_NORMAL)
        finder.search("o.parent.id", parentId, Finder.SearchType.EQ)
        finder.append("order by o.sort")
        officeList = sysAreaMapper!!.find(finder)
        return officeList
    }
    fun findNameByIdIn(ids:  List<String>): List<String> {
        return dao.findNameByIdIn(ids)
    }


}