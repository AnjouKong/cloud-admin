package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysOfficeService;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.security.SecurityUtils;
import cn.phenix.cloud.utils.TreeUtils;
import cn.phenix.model.sys.SysCategory;
import cn.phenix.model.sys.SysMenu;
import cn.phenix.model.sys.SysOffice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织管理
 *
 * @author xiaobin
 * @create 2017-09-26 下午2:50
 **/
@Controller
@RequestMapping("platform/sys/office")
@Api(tags = "组织管理")
public class OfficeController {

    @Autowired
    private SysOfficeService sysOfficeService;

    @GetMapping("")
    @ApiOperation(value = "默认界面")
    public String index(Model model) {
        model.addAttribute("obj", sysOfficeService.findOfficeListByParentId("0",SecurityUtils.getPrincipal().isAdmin()));
        return "sys/office/index";
    }

    @GetMapping("child/{id}")
    @ApiOperation(value = "组织树")
    public String child(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", sysOfficeService.findOfficeListByParentId(id, SecurityUtils.getPrincipal().isAdmin()));
        return "sys/office/child";
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "预览")
    public String detail(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", sysOfficeService.get(id));
        return "sys/office/detail";
    }

    @GetMapping("/add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "新增")
    public String add(String pid, Model model) {
        model.addAttribute("obj", StringUtils.isBlank(pid) ? null : sysOfficeService.get(pid));
        return "sys/office/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        SysOffice sysOffice = sysOfficeService.get(id);
        model.addAttribute("obj", sysOffice);
        model.addAttribute("parentUnit", sysOfficeService.get(sysOffice.getParentId()));
        return "sys/office/edit";
    }

    @PostMapping("/save")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public Result save(SysOffice office) {
        try {
            Subject subject = org.apache.shiro.SecurityUtils.getSubject();
            cn.phenix.cloud.core.security.Principal principal = (cn.phenix.cloud.core.security.Principal) subject.getPrincipal();
            if(principal != null){
                office.setUpdateBy(principal.getName());
            }else{
                office.setUpdateBy("");
            }
            SysOffice office1 = sysOfficeService.get(office.getId());
            if(office1 != null){
                office.setChildDeptList(office1.getChildDeptList());
            }
            sysOfficeService.save(office);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @GetMapping("/tree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("树加载")
    public List<Map<String, Object>> tree(String pid) {
        if (StringUtils.isBlank(pid))
            pid = SysOffice.DEL_FLAG_NORMAL;
        List<SysOffice> list = sysOfficeService.findOfficeListByParentId(pid,SecurityUtils.getPrincipal().isAdmin());
        List<Map<String, Object>> tree = new ArrayList<>();
        for (SysOffice unit : list) {
            tree.add(TreeUtils.treeMap(unit));
        }
        return tree;
    }

    @GetMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit.delete")
    @ApiOperation("删除")
    public Result delete(@PathVariable("id") String id) {
        if ("1".equals(id)) {
            return Result.error("不允许的操作");
        }
        sysOfficeService.deleteById(id);
        return Result.success();
    }

    /**
     * 供外部调用
     * @param pid
     * @return
     */
    @RequestMapping("treeData")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("树加载")
    public List<Map<String, Object>> treeData(String pid) {
        pid = pid == null ? SysMenu.ROOT_PARENT_ID : pid;
        List<SysOffice> list = sysOfficeService.findOfficeListByParentId(pid,SecurityUtils.getPrincipal().isAdmin());
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        if (Strings.isBlank(pid) || SysMenu.ROOT_PARENT_ID.equals(pid)) {
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

}
