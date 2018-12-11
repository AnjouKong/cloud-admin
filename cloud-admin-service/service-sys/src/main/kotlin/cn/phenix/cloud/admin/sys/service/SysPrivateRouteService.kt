package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysPrivateRouteMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.DataTableParameter
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.cloud.jpa.data.PageTable
import cn.phenix.model.sys.SysPrivateRoute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 私有路由
 *
 */
@Service
@Transactional(readOnly = true)
class SysPrivateRouteService : CoreService<SysPrivateRouteMapper, SysPrivateRoute>() {

    @Autowired
    private val sysPrivateRouteMapper: SysPrivateRouteMapper? = null

    fun findPage(parameter: DataTableParameter): PageTable<SysPrivateRoute> {
        val pageRequest = parameter.pageRequest
        val finder = Finder.create("from SysPrivateRoute ")
        finder.whereExcludeDel()
        return PageTable(dao.find(finder, pageRequest))
    }


}