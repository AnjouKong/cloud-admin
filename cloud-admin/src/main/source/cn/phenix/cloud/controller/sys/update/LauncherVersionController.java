package cn.phenix.cloud.controller.sys.update;

import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.admin.update.service.LauncherService;
import cn.phenix.cloud.admin.update.service.LauncherVersionDependService;
import cn.phenix.cloud.admin.update.service.LauncherVersionService;
import cn.phenix.cloud.admin.update.service.UpgradeStrategyService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.mechanism.AttMain;
import cn.phenix.model.upgrade.Launcher;
import cn.phenix.model.upgrade.LauncherVersion;
import cn.phenix.model.upgrade.LauncherVersionDepend;
import cn.phenix.model.upgrade.UpgradeStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("platform/sys/update/version")
@Api(tags = "终端版本管理")
@RequiresPermissions("sys.manager.unit")
public class LauncherVersionController extends BaseController {

    @Autowired
    private LauncherVersionService launcherVersionService;
    @Autowired
    private LauncherService launcherService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private UpgradeStrategyService upgradeStrategyService;
    @Autowired
    private LauncherVersionDependService launcherVersionDependService;


    @ApiOperation("首页跳转")
    @RequestMapping("")
    public String index(String launcherId,Model model,String versionId) {
        model.addAttribute("launcherId",launcherId);
        model.addAttribute("versionId",versionId);
        return "sys/update/version/index";
    }
    @RequestMapping("/versionTree")
    @ApiOperation("版本树加载")
    public String versionTree(String launcherId,Model model,String versionId) {
        model.addAttribute("launcherId",launcherId);
        model.addAttribute("versionId",versionId);
        return "sys/update/version/versionTree";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<LauncherVersion> data(DataTableParameter parameter, LauncherVersion launcherVersion,String launcherId) {
        PageTable<LauncherVersion> pageTable = launcherVersionService.findPage(parameter, launcherVersion,launcherId);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(String launcherId,Model model) {
        model.addAttribute("launcherId",launcherId);
        model.addAttribute("obj",launcherService.get(launcherId));
        return "sys/update/version/add";
    }

    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加数据")
    @RequiresPermissions("sys.manager.unit")
    @Transactional
    public Result addDo(LauncherVersion launcherVersion, String launcherId, AttMainForm attMainForm, LauncherVersionDepend launcherVersionDepend) {

        List<LauncherVersion> versionList =launcherVersionService.findByVersionAndDelFlagAndLauncherId(launcherVersion.getVersion(),"0",launcherId);
        if(versionList.size()>0){
            return Result.error("版本号已存在");
        }
        launcherVersion.setLauncher(launcherService.get(launcherId));//??
        attMainService.updateAttMainForm(attMainForm, launcherVersion.getId(), LauncherVersion.MODEL_NAME);
        launcherVersionService.save(launcherVersion);

        if(StringUtils.isNoneEmpty(launcherVersionDepend.getDependVersionId())){
            //保存依赖信息
            String[] dependVersionIds = launcherVersionDepend.getDependVersionId().split(",");
            String[] dependVersionNames = launcherVersionDepend.getDependVersionName().split(",");
            String[] dependLauncherNames = launcherVersionDepend.getDependLauncherName().split(",");
            String[] dependLauncherIds = launcherVersionDepend.getDependLauncherId().split(",");

            launcherVersionDependService.deleteByVersionId(launcherVersion.getId());
            for(int i=0;i<dependVersionIds.length;i++){
                LauncherVersionDepend launcherVersionDepend1 = new LauncherVersionDepend();
                launcherVersionDepend1.setDependVersionId(dependVersionIds[i]);
                launcherVersionDepend1.setVersionId(launcherVersion.getId());
                launcherVersionDepend1.setDependLauncherName(dependLauncherNames[i]);
                launcherVersionDepend1.setDependLauncherId(dependLauncherIds[i]);
                launcherVersionDepend1.setDependVersionName(dependVersionNames[i]);
                launcherVersionDependService.save(launcherVersionDepend1);
            }

        }
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        LauncherVersion launcherVersion = launcherVersionService.get(id);
        List<LauncherVersionDepend> launcherVersionDependList = launcherVersionDependService.findByVersionId(id);
//        List<AttMain> list = attMainService.getAttMain(id, LauncherVersion.MODEL_NAME, "version");
//        model.addAttribute("apk", list.size() > 0 ? list.get(0) : null);
        model.addAttribute("obj",launcherVersion);
        model.addAttribute("dependList",launcherVersionDependList.size()>0?launcherVersionDependList:null);
        return "sys/update/version/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(LauncherVersion launcherVersion,AttMainForm attMainForm,String oldVersion,String launcherId , LauncherVersionDepend launcherVersionDepend) {
        if(!oldVersion.equals(launcherVersion.getVersion())){
            List<LauncherVersion> versionList =launcherVersionService.findByVersionAndDelFlagAndLauncherId(launcherVersion.getVersion(),"0",launcherId);
            if(versionList.size()>0){
                return Result.error("版本号已存在");
            }
        }

        if(!attMainForm.getAttMains1().isEmpty()){
            attMainService.updateAttMainForm(attMainForm, launcherVersion.getId(), LauncherVersion.MODEL_NAME);
        }
        launcherVersion.setLauncher(launcherService.get(launcherId));//??
        List<String > idList = new ArrayList<>();
        idList.add(launcherVersion.getId());
        List<UpgradeStrategy> strategyList = upgradeStrategyService.findByLauncherVersionIdIn(idList);
        launcherVersion.setStrategyList(strategyList);
        launcherVersionService.save(launcherVersion);

        //保存依赖信息
        if(StringUtils.isNoneEmpty(launcherVersionDepend.getDependVersionId())){
            String[] dependVersionIds = launcherVersionDepend.getDependVersionId().split(",");
            String[] dependVersionNames = launcherVersionDepend.getDependVersionName().split(",");
            String[] dependLauncherNames = launcherVersionDepend.getDependLauncherName().split(",");
            String[] dependLauncherIds = launcherVersionDepend.getDependLauncherId().split(",");

            launcherVersionDependService.deleteByVersionId(launcherVersion.getId());
            for(int i=0;i<dependVersionIds.length;i++){
                LauncherVersionDepend launcherVersionDepend1 = new LauncherVersionDepend();
                launcherVersionDepend1.setDependVersionId(dependVersionIds[i]);
                launcherVersionDepend1.setVersionId(launcherVersion.getId());
                launcherVersionDepend1.setDependLauncherName(dependLauncherNames[i]);
                launcherVersionDepend1.setDependLauncherId(dependLauncherIds[i]);
                launcherVersionDepend1.setDependVersionName(dependVersionNames[i]);
                launcherVersionDependService.save(launcherVersionDepend1);
            }

        }

        return Result.success();
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        List<UpgradeStrategy> strategyList= upgradeStrategyService.findByLauncherVersionIdIn(Arrays.asList(ids));
        if(strategyList.size()>0){
            return Result.error("不能删除，此版本已被更新策略选中！");
        }
        launcherVersionService.deleteByIds(ids);
        for (String str : ids) {
            attMainService.deleteAttMain(str, LauncherVersion.MODEL_NAME);
        }
        return Result.success();
    }
    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public String detail(@PathVariable("id") String id,Model model) {
        List<LauncherVersionDepend> launcherVersionDependList = launcherVersionDependService.findByVersionId(id);
        List<AttMain> attMainList =attMainService.getAttMain(id,LauncherVersion.MODEL_NAME, "version");
        model.addAttribute("obj",  launcherVersionService.get(id));
        model.addAttribute("attMain",attMainList.size()>0?attMainList.get(0):null);
        model.addAttribute("dependList",launcherVersionDependList.size()>0?launcherVersionDependList:null);
        return "sys/update/version/detail";
    }

    /**
     * launcher
     * @param launcherId
     * @return
     */
    @GetMapping("/launcherTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("launcher树加载")
    public List<Map<String, Object>> launcherTree(String launcherId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        if(StringUtils.isBlank(launcherId)){
            List<Launcher> list = launcherService.findByDelFlag(Launcher.DEL_FLAG_NORMAL);
            for (Launcher launcher : list) {
                Map<String, Object> obj2 = new HashMap<>();
                obj2.put("id", launcher.getId());
                obj2.put("text", launcher.getName());
                obj2.put("children", true);
                tree.add(obj2);
            }
        }else{
            List<LauncherVersion> list = launcherVersionService.findByLauncherIdAndDelFlag(launcherId,Launcher.DEL_FLAG_NORMAL);
            for (LauncherVersion launcherVersion : list) {
                Map<String, Object> obj2 = new HashMap<>();
                obj2.put("id", launcherVersion.getId());
                obj2.put("text", launcherVersion.getVersion());
                obj2.put("children", false);
                tree.add(obj2);
            }
        }
        return tree;
    }

    /**
     * launcher
     * @param launcherId
     * @return
     */
    @GetMapping("/launcherTreeExceptSelf")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("launcher树加载")
    public List<Map<String, Object>> launcherTreeExceptSelf(String launcherId,String id) {
        List<Map<String, Object>> tree = new ArrayList<>();
        if(StringUtils.isBlank(launcherId)){
            List<Launcher> list = launcherService.findByDelFlagAndIdNotIn(Launcher.DEL_FLAG_NORMAL,id);
            for (Launcher launcher : list) {
                Map<String, Object> obj2 = new HashMap<>();
                obj2.put("id", launcher.getId());
                obj2.put("text", launcher.getName());
                obj2.put("children", true);
                tree.add(obj2);
            }
        }else{
            List<LauncherVersion> list = launcherVersionService.findByLauncherIdAndDelFlag(launcherId,Launcher.DEL_FLAG_NORMAL);
            for (LauncherVersion launcherVersion : list) {
                Map<String, Object> obj2 = new HashMap<>();
                obj2.put("id", launcherVersion.getId());
                obj2.put("text", launcherVersion.getVersion());
                obj2.put("children", false);
                tree.add(obj2);
            }
        }
        return tree;
    }

    /**
     * launcher
     * @param
     * @return
     */
    @GetMapping("/launcherTree4Version")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("版本下launcher树加载")
    public List<Map<String, Object>> launcherTree4Version() {
        List<Map<String, Object>> tree = new ArrayList<>();
        List<Launcher> list = launcherService.findByDelFlag(Launcher.DEL_FLAG_NORMAL);
        for (Launcher launcher : list) {
            Map<String, Object> obj2 = new HashMap<>();
            obj2.put("id", launcher.getId());
            obj2.put("text", launcher.getName());
            obj2.put("children", false);
            tree.add(obj2);
        }
        return tree;
    }
}
