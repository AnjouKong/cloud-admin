package cn.phenix.cloud.admin.update.service


import cn.phenix.cloud.admin.update.dao.LauncherVersionMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.DataTableParameter
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.cloud.jpa.data.PageTable
import cn.phenix.model.upgrade.LauncherVersion
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
@Transactional(readOnly = true)
class LauncherVersionService : CoreService<LauncherVersionMapper, LauncherVersion>() {

    fun findPage(parameter: DataTableParameter, launcherVersion: LauncherVersion, launcherId: String): PageTable<LauncherVersion> {

        val pageRequest = parameter.pageRequest
        val finder = Finder.create("from LauncherVersion ")
        finder.whereExcludeDel()
        finder.search("launcherId", launcherId, Finder.SearchType.EQ)
        finder.search("remarks", launcherVersion.remarks, Finder.SearchType.LIKE)
        finder.search("version", launcherVersion.version, Finder.SearchType.LIKE)

        val page = dao.find(finder, pageRequest)
        return PageTable(page)
    }


    fun findByStrategyIdIn(ids: Array<String>): List<LauncherVersion> {
        return dao.findByStrategyIdIn(ids)
    }

    @Transactional
    fun updateByLauncherId(id: String) {
        dao.updateByLauncherId(id)
    }

    fun findByLauncherIdIn(ids: Array<String>): List<LauncherVersion> {
        return dao.findByLauncherIdIn(ids)
    }
     fun findIdByLauncherIdIn(ids: Array<String>): List<LauncherVersion> {
        return dao.findIdByLauncherIdIn(ids)
    }

    fun findByLauncherIdAndDelFlag(launcherId: String,delFlag: String): List<LauncherVersion> {
        return dao.findByLauncherIdAndDelFlag(launcherId,delFlag)
    }
    fun findByVersionAndDelFlagAndLauncherId(version: String,delFlag: String,launcherId: String): List<LauncherVersion> {

        val finder = Finder.create("from LauncherVersion ")
        finder.whereExcludeDel()
        finder.search("version", version)
        finder.search("launcher.id", launcherId)

        return dao.find(finder)
    }
}
