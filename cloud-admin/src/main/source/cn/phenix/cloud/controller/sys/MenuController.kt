package cn.phenix.cloud.controller.sys

import cn.phenix.cloud.admin.sys.service.SysMenuService
import cn.phenix.cloud.base.web.BaseController
import cn.phenix.cloud.core.api.Result
import cn.phenix.cloud.core.utils.Strings
import cn.phenix.cloud.security.SecurityUtils
import cn.phenix.cloud.utils.TreeUtils
import cn.phenix.model.sys.SysMenu
import com.google.common.collect.Lists
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.commons.lang3.StringUtils
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

/**
 * @author xiaobin
 * @create 2017-09-29 下午3:58
 */
@Controller
@Api(tags = arrayOf("菜单管理"))
@RequestMapping("platform/sys/menu")
class MenuController : BaseController() {

    @Autowired
    private val sysMenuService: SysMenuService? = null

    @GetMapping("")
    @ApiOperation("首页跳转")
    fun index(model: Model): String {
        val list = sysMenuService!!.findByUserId(SecurityUtils.getPrincipal()!!.id, SysMenu.ROOT_PARENT_ID, true)
        model.addAttribute("list", list)
        return "sys/menu/index"
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.menu")
    @ApiOperation("添加页面跳转")
    fun add(pid: String?, model: Model): String {
        model.addAttribute("obj", if (StringUtils.isBlank(pid)) null else sysMenuService!!.get(pid!!))
        return "sys/menu/add"
    }

    @PostMapping("save")
    @ResponseBody
    @ApiOperation("添加")
    @RequiresPermissions("sys.manager.menu.add")
    fun save(menu: SysMenu): Result {
        menu.isShow = ("data" != menu.type)
        menu.sort = 0
        if (menu.parent == null || StringUtils.isBlank(menu.parent.id)) {
            menu.parent = SysMenu(SysMenu.ROOT_PARENT_ID)
        }
        sysMenuService!!.save(menu)
        return Result.success("system.success")
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.menu")
    @ApiOperation("编辑页面跳转")
    fun edit(@PathVariable("id") id: String, model: Model): String {
        val menu = sysMenuService!!.get(id)
        model.addAttribute("obj", menu)
        return "sys/menu/edit"
    }

    @PostMapping("update")
    @ResponseBody
    @RequiresPermissions("sys.manager.menu.edit")
    @ApiOperation("编辑数据")
    fun update(menu: SysMenu): Result {
        buildModel(menu)
        sysMenuService!!.update(menu)
        return Result.success("成功")
    }

    @GetMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.menu.edit")
    fun delete(@PathVariable("id") id: String, model: Model): Result {
        sysMenuService!!.deleteById(id)
        return Result.success("成功")
    }

    @GetMapping("enable/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.menu.edit")
    @ApiOperation("启用")
    fun enable(@PathVariable("id") id: String, model: Model): Result {
        val menu = sysMenuService!!.get(id)
        menu.isShow = true
        sysMenuService!!.update(menu)
        return Result.success("成功")
    }

    @GetMapping("disable/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.menu.edit")
    @ApiOperation("禁用")
    fun disable(@PathVariable("id") id: String, model: Model): Result {
        val menu = sysMenuService!!.get(id)
        menu.isShow = false
        sysMenuService!!.update(menu)
        return Result.success("成功")
    }


    @GetMapping("tree")
    @ResponseBody
    @RequiresPermissions("sys.manager.menu")
    @ApiOperation("树加载")
    fun tree(pid: String?): List<Map<String, Any>> {
        var pid = pid
        if (StringUtils.isBlank(pid))
            pid = SysMenu.ROOT_PARENT_ID
        val list = sysMenuService!!.findByUserId(SecurityUtils.getPrincipal()!!.id, pid, true)
        val tree = Lists.newArrayList<Map<String, Any>>()
        list.mapTo(tree) { TreeUtils.treeMap(it) }
        return tree
    }

    @GetMapping("/child/{id}")
    @RequiresPermissions("sys.manager.menu")
    @ApiOperation("子菜单加载")
    fun child(@PathVariable("id") id: String, model: Model): String {
        val m = sysMenuService!!.get(id)
        val list = Lists.newArrayList<SysMenu>()
        val menus = sysMenuService!!.findByUserId(SecurityUtils.getPrincipal()!!.id, id, null)
        val datas = sysMenuService!!.findByPathAndType(Strings.sBlank(m.path) + "________", "data")
        for (menu in menus) {
            for (bt in datas) {

                if (menu.path == bt.path.substring(0, bt.path.length - 4)) {
                    menu.hasChildren = true
                    break
                }
            }
            list.add(menu)
        }
        model.addAttribute("obj", list)
        return "sys/menu/child"
    }


    @GetMapping("sort")
    @RequiresPermissions("sys.manager.menu")
    @ApiOperation("排序页面跳转")
    fun sort(model: Model): String {
        val list = sysMenuService!!.findMenuList(SysMenu.ROOT_PARENT_ID)
        model.addAttribute("firstMenus", list)
        return "sys/menu/sort"
    }

    @PostMapping("sortDo")
    @RequiresPermissions("sys.manager.menu.edit")
    @ApiOperation("排序")
    @ResponseBody
    fun sortDo(ids: String): Result {
        sysMenuService!!.sortDo(ids)
        return Result.success("system.success")
    }

}
