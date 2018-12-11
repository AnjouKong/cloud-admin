package cn.phenix.cloud.admin.update.service


import cn.phenix.cloud.admin.update.dao.LauncherMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.DataTableParameter
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.cloud.jpa.data.PageTable
import cn.phenix.model.upgrade.Launcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 2018年1月3日
 */
@Service
@Transactional(readOnly = true)
class LauncherService : CoreService<LauncherMapper, Launcher>() {
    @Autowired
    private val launcherMapper: LauncherMapper? = null


    fun findPage(parameter: DataTableParameter, launcher: Launcher): PageTable<Launcher> {

        val pageRequest = parameter.pageRequest
        val finder = Finder.create("from Launcher ")
        finder.whereExcludeDel()
        finder.search("name",launcher.name)

        val page = launcherMapper!!.find(finder, pageRequest)
        return PageTable(page)
    }

    fun findByDelFlag(delFlag: String): List<Launcher> {
        return launcherMapper!!.findByDelFlag(delFlag)
    }
    fun findByDelFlagAndIdNotIn(delFlag: String,id: String): List<Launcher> {
        return launcherMapper!!.findByDelFlagAndIdNotIn(delFlag,id)
    }

    fun findByCodeAndDelFlag(code: String,delFlag: String): List<Launcher> {
        return launcherMapper!!.findByCodeAndDelFlag(code,delFlag)
    }
}
