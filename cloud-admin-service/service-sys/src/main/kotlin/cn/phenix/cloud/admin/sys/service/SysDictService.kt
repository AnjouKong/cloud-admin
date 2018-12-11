package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysDictMapper
import cn.phenix.cloud.core.utils.Strings
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.model.sys.SysDict
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author xiaobin
 * @create 2017-10-09 下午4:59
 */
@Service
@Transactional(readOnly = true)
class SysDictService : CoreService<SysDictMapper, SysDict>() {


    @Transactional
    fun sortDo(ids: String) {
        val menuIds = StringUtils.split(ids, ",")
        dao.updateAllSort(0)
        menuIds
                .filterNot { Strings.isBlank(it) }
                .forEachIndexed { i, s -> dao.updateSort(i, s) }
    }

    fun findByCode(code: String): List<SysDict> {
        return dao.findByCodeAndDelFlag(code, SysDict.DEL_FLAG_NORMAL)
    }

    fun findByCode(code: String, id: String): List<SysDict> {
        val finder = Finder.create("from SysDict ")
        finder.whereExcludeDel()
        finder.search("code", code)
        finder.search("id", id, Finder.SearchType.NEQ)
        return dao.find(finder)
    }

    fun findRootDic(parentId: String): List<SysDict> {
        val finder = Finder.create("from SysDict ")
        finder.whereExcludeDel()
        if (Strings.isBlank(parentId)) {
            finder.append(" and (parent.id is null or parent.id='' )")
        } else {
            finder.search("parent.id", parentId)
        }
        return dao.find(finder)
    }
    fun findByParentId(parentId: String?): List<SysDict> {
        val finder = Finder.create("from SysDict ")
        finder.whereExcludeDel()
        if (Strings.isBlank(parentId)) {
            finder.append(" and (parent.id is null or parent.id='' )")
        } else {
            finder.search("parent.id", parentId)
        }
        return dao.find(finder)
    }
}
