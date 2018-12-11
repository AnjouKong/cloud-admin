package cn.phenix.cloud.controller.sys.update;


import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.update.service.LauncherService;
import cn.phenix.cloud.admin.update.service.LauncherVersionService;
import cn.phenix.cloud.admin.update.service.UpgradeStrategyService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.upgrade.Launcher;
import cn.phenix.model.upgrade.LauncherVersion;
import cn.phenix.model.upgrade.UpgradeStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("platform/sys/update")
@Api(tags = "终端版本管理")
@RequiresPermissions("sys.manager.unit")
public class LauncherController extends BaseController {

    @Autowired
    private LauncherService launcherService;
    @Autowired
    private LauncherVersionService launcherVersionService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private UpgradeStrategyService upgradeStrategyService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "sys/update/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<Launcher> data(DataTableParameter parameter, Launcher launcher) {
        PageTable<Launcher> pageTable = launcherService.findPage(parameter, launcher);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(Model model) {
        return "sys/update/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @ApiOperation("添加数据")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(Launcher launcher) {
        List<Launcher> launcherList =launcherService.findByCodeAndDelFlag(launcher.getCode(),"0");
        if(launcherList.size()>0){
            return Result.error("应用标识已存在");
        }
        launcherService.save(launcher);
        return Result.success();
    }

    @GetMapping("/version/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("版本列表首页")
    public String versionList(@PathVariable("id") String id,Model model) {
        model.addAttribute("launcherId",id);
        return "sys/update/version/index";
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        //删除上传包
        List<LauncherVersion> versionList =launcherVersionService.findByLauncherIdIn(ids);
        for(int i=0;i<versionList.size();i++){
            attMainService.deleteAttMain(versionList.get(i).getId(), LauncherVersion.MODEL_NAME);
        }
        //删除版本
        List<LauncherVersion> versionsList= launcherVersionService.findIdByLauncherIdIn(ids);
        List<String> versionIdList = new ArrayList<>();
        for(LauncherVersion version :versionsList){
            versionIdList.add(version.getId());
        }
        List<UpgradeStrategy> strategyList= upgradeStrategyService.findByLauncherVersionIdIn(versionIdList);
        if(strategyList.size()>0){
            return Result.error("不能删除，此版本已被更新策略选中！");
        }
        for (String id : ids) {
            launcherVersionService.updateByLauncherId(id);
        }
        //删除应用
        launcherService.deleteByIds(ids);

        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", launcherService.get(id));
        return "sys/update/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(Launcher launcher,String oldCode) {
        if(!launcher.getCode().equals(oldCode)){
            List<Launcher> launcherList =launcherService.findByCodeAndDelFlag(launcher.getCode(),"0");
            if(launcherList.size()>0){
                return Result.error("应用标识已存在");
            }
        }
        List<LauncherVersion> childDeptList = launcherVersionService.findByLauncherIdAndDelFlag(launcher.getId(),"0");
        launcher.setChildDeptList(childDeptList);
        launcherService.save(launcher);
        return Result.success();
    }

}
