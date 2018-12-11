package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysMenuService;
import cn.phenix.cloud.admin.sys.service.SysOfficeService;
import cn.phenix.cloud.admin.sys.service.SysRoleService;
import cn.phenix.cloud.admin.sys.service.SysUserService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.config.Global;
import cn.phenix.cloud.core.utils.JsonUtils;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.cloud.security.SecurityUtils;
import cn.phenix.model.sys.SysMenu;
import cn.phenix.model.sys.SysRole;
import cn.phenix.model.sys.SysUser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-10-08 上午9:08
 **/
@Controller
@RequestMapping("platform/sys/role")
@Api(tags = "组织管理")
public class RoleController extends BaseController {

    @GetMapping("")
    @RequiresPermissions("sys.manager.role")
    @ApiOperation("首页跳转")
    public String index() {
        return "sys/role/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    public PageTable<SysRole> data(String officeId, int draw, DataTableParameter parameter) {
        if(officeId.equals("root")) {
            officeId="";
        }
        return sysRoleService.findPage(officeId, parameter);
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.role")
    @ApiOperation("添加数据")
    public String add(String officeId, Model model) {
        List<SysMenu> menuList = sysMenuService.findMenuList(null);
        //List<SysMenu> datas = sysMenuService.findMenuByUserIdAndType(SecurityUtils.getPrincipal().getId(), "data");
        List<Map<String, Object>> menus = buildMenu(menuList, null);
        model.addAttribute("menus", JsonUtils.safeObjectToJson(menus));
        model.addAttribute("obj", officeId==null?null:sysOfficeService.get(officeId));
        return "sys/role/add";
    }

    @RequestMapping("editMenu/{roleId}")
    @RequiresPermissions("sys.manager.role")
    @ApiOperation("编辑菜单页面跳转")
    public Object editMenu(@PathVariable("roleId") String roleId, Model model) {
        StringBuilder roleMenuIds = new StringBuilder();
        List<SysMenu> list = sysMenuService.findMenuList(null);
        List<SysMenu> roleMenu = sysMenuService.findMenusAndButtonsByRole(roleId, null);
        for (SysMenu m : roleMenu) {
            roleMenuIds.append(m.getId() + "#");
        }
        String roleMenuId = roleMenuIds.toString();
        List<Map<String, Object>> menus = buildMenu(list, roleMenuId);

        model.addAttribute("menus", JsonUtils.safeObjectToJson(menus));
        model.addAttribute("obj", sysRoleService.get(roleId));
        return "sys/role/editMenu";
    }

    @ResponseBody
    @RequestMapping("editMenuDo")
    @ApiOperation("编辑菜单数据")
    @RequiresPermissions("sys.manager.role.menu")
    public Result editMenuDo(String menuIds, String roleid, Model model) {
        String[] ids = StringUtils.split(menuIds, ",");
        SysRole role = sysRoleService.updateMenuRole(ids, roleid);
        model.addAttribute("name", role.getName());
        return Result.success("system.success");
    }

    @GetMapping("editUser/{roleId}")
    @ApiOperation("编辑user")
    @RequiresPermissions("sys.manager.role")
    public String editUser(@PathVariable("roleId") String roleId, Model model) {
        model.addAttribute("obj", sysRoleService.get(roleId));
        return "sys/role/editUser";
    }

    @GetMapping("selectUser")
    @ApiOperation("选择user")
    @RequiresPermissions("sys.manager.role")
    public String selectUser() {
        return "sys/role/selectUser";
    }


    @PostMapping("userData")
    @ResponseBody
    @ApiOperation("user数据加载")
    @RequiresPermissions("sys.manager.role")
    public PageTable<SysUser> userData(String roleid, String loginname, String nickname, DataTableParameter parameter) {
        Finder finder = Finder.create("select user from SysUser user join user.roleList role");
        finder.whereExcludeDel("user");
        finder.search("role.id", roleid);
        finder.search("user.loginName", loginname, Finder.SearchType.LIKE);
        finder.search("user.name", nickname, Finder.SearchType.LIKE);
        return sysUserService.findPageByFinder(finder, parameter);
    }

    @PostMapping("pushUser")
    @ResponseBody
    @ApiOperation("pushUser")
    @RequiresPermissions("sys.manager.role.user")
    public Result pushUser(String userIds, String roleId) {
        String[] ids = StringUtils.split(userIds, ",");
        sysRoleService.pushUser(ids, roleId);
        return Result.success();
    }

    @PostMapping("selectData")
    @ResponseBody
    @ApiOperation("选择数据")
    @RequiresPermissions("sys.manager.role")
    public PageTable<Map> selectData(String roleid, String name, DataTableParameter parameter) {
        Parameter param = Parameter.create("findUserDataByRoleIdAndName").insertExcludeNull("roleId", roleid);
        param.insertExcludeNull("name", "%" + name + "%");
        return sysUserService.findPageByNamedQuery(param, parameter.getPageRequest(), Map.class);
    }

    @PostMapping("delUser")
    @ResponseBody
    @ApiOperation("删除user")
    @RequiresPermissions("sys.manager.role.user")
    public Result delUser(String userIds, String roleId) {
        String[] ids = StringUtils.split(userIds, ",");
        sysRoleService.deleteUser(ids, roleId);
        return Result.success();
    }

    private List<Map<String, Object>> buildMenu(List<SysMenu> list, String roleMenuId) {
        List<Map<String, Object>> menus = Lists.newArrayList();
        for (SysMenu menu : list) {
            Map<String, Object> map = Maps.newHashMap();

            String parentId = menu.getParentId();
            if (StringUtils.isBlank(parentId) || "0".equals(parentId)) {
                parentId = "#";
            }
            map.put("id", menu.getId());
            map.put("text", menu.getName());
            map.put("icon", Strings.sBlank(menu.getIcon()));
            map.put("parent", parentId);
            map.put("data", menu.getHref());
            Map<String, Object> state = Maps.newConcurrentMap();
            if (roleMenuId != null && roleMenuId.contains(menu.getId() + "#")) {
                state.put("selected", true);
                map.put("state", state);
            } else {
                state.put("selected", false);
                map.put("state", state);
            }
            menus.add(map);
        }
        return menus;
    }

    @RequiresPermissions("sys.manager.role.add")
    @RequestMapping(value = "save")
    @ResponseBody
    @ApiOperation("保存")
    public Result save(SysRole role) {
        if (!SecurityUtils.getPrincipal().isAdmin() && role.getSysData().equals(Global.YES)) {
            return Result.error("越权操作，只有超级管理员才能修改此数据！");
        }
        if (sysRoleService.findByRoleName(role.getName()) != null) {
            return Result.error("保存角色'" + role.getName() + "'失败, 角色名已存在");
        }
        if (sysRoleService.findByRoleEnname(role.getEnname()) != null) {
            return Result.error("保存角色'" + role.getName() + "'失败, 角色编号已存在");
        }
        buildModel(role);
        sysRoleService.saveRole(role);
        return Result.success("保存角色成功");
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    @ApiOperation("删除")
    @RequiresPermissions("sys.manager.role.delete")
    public Object deletes(String[] roleIds) {
        if (!SecurityUtils.getPrincipal().isAdmin()) {
            return Result.error("越权操作，只有超级管理员才能修改此数据！");
        }
        SysRole sysRole = sysRoleService.getRoleBySys();
        for (String id : roleIds) {
            if (id.equals(sysRole.getId())) {
                return Result.error("不允许操作");
            }
        }
        sysRoleService.delete(roleIds);
        return Result.success("操作成功");
    }

    @RequestMapping(value = "edit/{roleId}")
    @RequiresPermissions("sys.manager.role")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("roleId") String roleId, Model model) {
        SysRole role = sysRoleService.get(roleId);
        model.addAttribute("unit", sysOfficeService.get(role.getOffice().getId()));
        model.addAttribute("obj", role);
        return "sys/role/edit";
    }

    @RequestMapping(value = "menu/{id}")
    @ApiOperation("menu数据加载")
    @RequiresPermissions("sys.manager.role")
    public String menu(@PathVariable("id") String id, Model model) {
        SysRole role = sysRoleService.get(id);
        List<SysMenu> menus = sysMenuService.findMenusAndButtonsByRole(id, null);
        //List<SysMenu> datas = sysMenuService.findMenusAndButtonsByRole(id, "data");
        List<SysMenu> firstMenus = new ArrayList<>();
        List<SysMenu> secondMenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.getParent().getId().equals("0")) {
                firstMenus.add(menu);
            } else {
                secondMenus.add(menu);
            }
        }
        model.addAttribute("userFirstMenus", firstMenus);
        model.addAttribute("userSecondMenus", secondMenus);
        model.addAttribute("jsonSecondMenus", JsonUtils.safeObjectToJson(secondMenus));
        model.addAttribute("obj", role);
        return "sys/role/menu";
    }

    @RequestMapping("/enable/{id}")
    @RequiresPermissions("sys.manager.user.edit")
    @ResponseBody
    @ApiOperation("启用")
    public Result enable(@PathVariable("id") String id, HttpServletRequest req) {
        SysRole sysRole = sysRoleService.get(id);
        sysRole.setUseable("1");
        sysRoleService.saveRole(sysRole);
        return Result.success("成功");
    }
    @RequestMapping("/disable/{id}")
    @RequiresPermissions("sys.manager.user.edit")
    @ResponseBody
    @ApiOperation("禁用")
    public Result disable(@PathVariable("id") String id, HttpServletRequest req) {
        SysRole sysRole = sysRoleService.get(id);
        sysRole.setUseable("0");
        sysRoleService.saveRole(sysRole);
        return Result.success("成功");
    }

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysOfficeService sysOfficeService;

}
