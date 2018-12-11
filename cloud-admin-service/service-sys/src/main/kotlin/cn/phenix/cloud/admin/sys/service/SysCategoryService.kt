package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysCategoryMapper
import cn.phenix.cloud.core.utils.Strings
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.model.sys.SysCategory
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author fxq
 * @create
 */
@Service
@Transactional(readOnly = true)
class SysCategoryService : CoreService<SysCategoryMapper, SysCategory>() {


    @Transactional
    fun sortDo(ids: String, modelName: String) {
        val menuIds = StringUtils.split(ids, ",")
        dao.updateAllSort(0, modelName)
        menuIds
                .filterNot { Strings.isBlank(it) }
                .forEachIndexed { i, s -> dao.updateSort(i, s) }
    }

    fun findRootDic(parentId: String, modelName: String): List<SysCategory> {
        val finder = Finder.create("from SysCategory ")
        finder.whereExcludeDel()
        finder.search("modelName", modelName)
        if (Strings.isBlank(parentId)) {
            finder.append(" and (parent.id is null or parent.id='' )")
        } else {
            finder.search("parent.id", parentId)
        }
        finder.append(" order by sort")
        return dao.find(finder)
    }

}