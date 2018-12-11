package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysOfficeService;
import cn.phenix.cloud.admin.sys.service.SysUserService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.security.Principal;
import cn.phenix.cloud.core.utils.MD5Util;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.security.SecurityUtils;
import cn.phenix.model.sys.SysOffice;
import cn.phenix.model.sys.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *
 * @author xiaobin
 * @create 2017-09-27 上午10:25
 **/
@Controller
@Api(tags = "用户管理")
@RequestMapping("platform/sys/user")
public class UserController extends BaseController {

    @Autowired
    private SysOfficeService sysOfficeService;
    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("")
    @ApiOperation("首页页面跳转")
    public String index() {
        return "sys/user/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<SysUser> data(String officeId, String loginName, String name, Integer draw,
                                   DataTableParameter parameter) {
        if(officeId.equals("root")) {
            officeId="";
        }
        PageTable<SysUser> pageTable = sysUserService.findPage(officeId, loginName, name, parameter);
        pageTable.setDraw(draw);
        return pageTable;


    }

    @RequestMapping("add")
    @ApiOperation("添加页面跳转")
    @RequiresPermissions("sys.manager.unit")
    public String add(String officeId, Model model) {
        model.addAttribute("obj", Strings.isBlank(officeId) ? null : sysOfficeService.get(officeId));
        return "sys/user/add";
    }

    @RequestMapping("edit/{id}")
    @ApiOperation("编辑")
    @RequiresPermissions("sys.manager.unit")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", sysUserService.get(id));
        return "sys/user/edit";
    }

    @RequiresPermissions("sys.manager.unit")
    @RequestMapping(value = "save")
    @ApiOperation("添加保存")
    @ResponseBody
    public Result save(SysUser user) {
        SysUser user1 = sysUserService.findByLoginNameAndDelFlag(user.getLoginName(),"0");
        if(user1 != null){
            return Result.error("用户名已存在");
        }
        user.setPassword(MD5Util.getMD5String(user.getPassword()));
        buildModel(user);
        sysUserService.save(user);
        return Result.success("成功");
    }

    @RequiresPermissions("sys.manager.unit")
    @RequestMapping(value = "update")
    @ResponseBody
    @ApiOperation("编辑保存")
    public Result update(SysUser user, String oldLoginName) {
        if(!oldLoginName.equals(user.getLoginName())){
            SysUser user1 = sysUserService.findByLoginNameAndDelFlag(user.getLoginName(),"0");
            if(user1 != null){
                return Result.error("用户名已存在");
            }
        }
        user.setPassword(MD5Util.getMD5String(user.getPassword()));
        buildModel(user);
        sysUserService.update(user);
        return Result.success("成功");
    }

    @RequiresAuthentication
    @RequestMapping(value = "pass")
    @ApiOperation("页面跳转")
    public String pass() {
        return "sys/user/pass";
    }

    @RequiresPermissions("sys.manager.unit")
    @RequestMapping(value = "resetPwd/{id}")
    @ResponseBody
    @ApiOperation("重置密码")
    public Result resetPwd(@PathVariable("id") String id, HttpServletRequest req) {
        SysUser user = sysUserService.get(id);
        user.setPassword(MD5Util.getMD5String("123456"));
        sysUserService.update(user);
        return Result.success("成功", "123456");
    }

    @RequestMapping(value = "detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("预览详情")
    public String detail(@PathVariable("id") String id, Model model) {
        if (!Strings.isBlank(id)) {
            SysUser user = sysUserService.get(id);
            model.addAttribute("obj", user);
        }
        return "/sys/user/detail";
    }

    @RequestMapping("/enable/{userId}")
    @RequiresPermissions("sys.manager.unit")
    @ResponseBody
    @ApiOperation("启用")
    public Result enable(@PathVariable("userId") String userId, HttpServletRequest req) {
        SysUser sysUser = sysUserService.get(userId);
        req.setAttribute("loginName", sysUser.getLoginName());
        sysUser.setLoginFlag("1");
        sysUserService.update(sysUser);
        return Result.success("成功");
    }


    @RequestMapping(value = "/disable/{userId}")
    @RequiresPermissions("sys.manager.unit")
    @ResponseBody
    @ApiOperation("禁用")
    public Result disable(@PathVariable("userId") String userId, HttpServletRequest req) {
        SysUser sysUser = sysUserService.get(userId);
        if (sysUser.isAdmin()) {
            return Result.error("不允许操作");
        }
        req.setAttribute("loginName", sysUser.getLoginName());
        sysUser.setLoginFlag("0");
        sysUserService.update(sysUser);
        return Result.success("成功");
    }

    @GetMapping("/delete/{userId}")
    @RequiresPermissions("sys.manager.unit")
    @ResponseBody
    @ApiOperation("删除")
    public Result delete(@PathVariable("userId") String userId, HttpServletRequest req) {
        SysUser user = sysUserService.get(userId);
        if (user.isAdmin()) {
            return Result.error("不允许操作");
        }
        sysUserService.delete(user);
        req.setAttribute("loginName", user.getLoginName());
        return Result.success("成功");
    }

    @GetMapping("/tree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("树加载")
    public List<Map<String, Object>> tree(@Param("pid") String pid) {
        if(Strings.isBlank(pid)) pid="";
        List<SysOffice> list = sysOfficeService.findOfficeListByParentId(pid, SecurityUtils.getPrincipal().isAdmin());
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        if (Strings.isBlank(pid)) {
            obj.put("id", "root");
            obj.put("text", "所有用户");
            obj.put("children", false);
            tree.add(obj);
        }
        for (SysOffice unit : list) {
            obj = new HashMap<>();
            obj.put("id", unit.getId());
            obj.put("text", unit.getName());
            obj.put("children", true);
            tree.add(obj);
        }
        return tree;
    }

    @PostMapping("/delete")
    @RequiresPermissions("sys.manager.unit")
    @ResponseBody
    @ApiOperation("批量删除")
    public Result deletes(String[] ids, HttpServletRequest req) {
        SysUser user = sysUserService.findByLoginName("admin");
        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            if (s.equals(user.getId())) {
                return Result.error("不允許操作");
            }
            sb.append(s).append(",");
        }
        sysUserService.deleteByIds(ids);
        req.setAttribute("ids", sb.toString());
        return Result.success("成功");
    }

    @PostMapping("/doChangePassword")
    @ResponseBody
    @RequiresAuthentication
    @ApiOperation("更改密码")
    public Result doChangePassword(@Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword, HttpServletRequest req) {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        Principal principal = (Principal) subject.getPrincipal();
        SysUser user = sysUserService.findByLoginNameAndDelFlag(principal.getLoginName(),"0");
        if(user == null){
            return Result.error("系统错误");
        }
        String old = MD5Util.getMD5String(oldPassword);
        String userId = user.getId();
        if (old.equals(user.getPassword())) {
            String newPasswordMD5 = MD5Util.getMD5String(newPassword);
            sysUserService.doChangePassword(newPasswordMD5,userId);
            return Result.success("成功");
        } else {
            return Result.error("原密码不正确");
        }
    }
}
