package cn.phenix.cloud.controller.sys

import cn.phenix.cloud.admin.sys.service.SysGetWayService
import cn.phenix.cloud.base.web.BaseController
import cn.phenix.cloud.core.api.Result
import cn.phenix.cloud.jpa.data.DataTableParameter
import cn.phenix.cloud.jpa.data.PageTable
import cn.phenix.model.sys.SysGetWay
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

/**
 * 智能路由管理器
 */
@Controller
@Api(tags = arrayOf("智能路由管理"))
@RequestMapping("platform/sys/getway")
class GetWayController : BaseController() {


    @GetMapping("")
    @RequiresPermissions("sys.manager.getway")
    @ApiOperation("首页跳转")
    fun index(): String = "/sys/getway/index"

    @GetMapping("add")
    @ApiOperation("添加页面跳转")
    @RequiresPermissions("sys.manager.getway")
    fun add(): String = "/sys/getway/add"

    @GetMapping("edit/{id}")
    @ApiOperation("编辑页面跳转")
    @RequiresPermissions("sys.manager.getway")
    fun edit(@PathVariable("id")id: String, model: Model): String {
        model.addAttribute("obj", sysGetWayService?.get(id))
        return "sys/getway/edit"
    }

    @PostMapping("addDo")
    @ResponseBody
    @ApiOperation("添加数据")
    @RequiresPermissions("sys.manager.getway")
    fun addDo(getWay: SysGetWay): Result {
        sysGetWayService?.save(getWay)
        return Result.success()
    }

    @PostMapping("editDo")
    @ResponseBody
    @ApiOperation("编辑数据")
    @RequiresPermissions("sys.manager.getway")
    fun editDo(getWay: SysGetWay): Result {
        sysGetWayService?.saveOrUpdate(getWay)
        return Result.success()
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @ApiOperation("删除数据")
    @RequiresPermissions("sys.manager.getway")
    fun delete(@PathVariable("id") id: String): Result {
        sysGetWayService?.deleteById(id)
        return Result.success()
    }

    @ResponseBody
    @RequestMapping("data")
    @ApiOperation("首页数据加载")
    @RequiresPermissions("sys.manager.getway")
    fun data(getWay: SysGetWay,
             parameter: DataTableParameter): PageTable<SysGetWay>? {
        return sysGetWayService?.findPage(null, parameter)
    }


    @Autowired
    private val sysGetWayService: SysGetWayService? = null
}