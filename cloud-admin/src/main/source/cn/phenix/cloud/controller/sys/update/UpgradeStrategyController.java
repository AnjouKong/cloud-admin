package cn.phenix.cloud.controller.sys.update;

import cn.phenix.cloud.admin.app.brand.service.BrandService;
import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationService;
import cn.phenix.cloud.admin.sys.service.SysAreaService;
import cn.phenix.cloud.admin.tenant.renter.service.TenancyService;
import cn.phenix.cloud.admin.update.service.*;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.upgrade.StrategyBrandSpec;
import cn.phenix.model.upgrade.StrategyCity;
import cn.phenix.model.upgrade.StrategyDeviceGroup;
import cn.phenix.model.upgrade.UpgradeStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("platform/sys/update/strategy")
@Api(tags = "版本升级策略")
@RequiresPermissions("sys.manager.unit")
public class UpgradeStrategyController extends BaseController {

    @Autowired
    private UpgradeStrategyService upgradeStrategyService;
    @Autowired
    private TenancyService tenancyService;
    @Autowired
    private LauncherVersionService launcherVersionService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private BrandSpecificationService brandSpecificationService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private StrategyBrandSpecService strategyBrandSpecService;
    @Autowired
    private StrategyCityService strategyCityService;
    @Autowired
    private StrategyDeviceGroupService strategyDeviceGroupService;

    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(Model model ) {
        return "sys/update/strategy/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<UpgradeStrategy> data(DataTableParameter parameter, UpgradeStrategy upgradeStrategy,String versionId,String launcherId ) {
        PageTable<UpgradeStrategy> pageTable = upgradeStrategyService.findPage(parameter, upgradeStrategy,versionId,launcherId);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(String versionId,Model model) {
        model.addAttribute("versionId",versionId);
        return "sys/update/strategy/add";
    }

    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation("添加数据")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(UpgradeStrategy upgradeStrategy,String[] tenantId,String[] specificationId,String[] areaId,String launcherVersionId) {
        //商户
//        List<Tenancy> tenancyList = tenancyService.findByIdIn(tenantId);
//        upgradeStrategy.setTenancyList(tenancyList);
        //商户-终端组
        List<StrategyDeviceGroup> groupList = new ArrayList<>();
        if(tenantId.length>0) {
            //商户-终端组
            for(int i=0;i<tenantId.length;i++){
                StrategyDeviceGroup strategyDeviceGroup = new StrategyDeviceGroup();
                strategyDeviceGroup.setUpgradeStrategyId(upgradeStrategy.getId());

                String[] pattern = tenantId[i].split("#");
                strategyDeviceGroup.setName(pattern[2]);
                strategyDeviceGroup.setTenantId(pattern[0]);
                strategyDeviceGroup.setGroupId(pattern[1]);
                groupList.add(strategyDeviceGroup);
            }
        }
        //地区
        List<StrategyCity> cityList = new ArrayList<>();
        for(int i=0;i<areaId.length;i++){
            StrategyCity strategyCity = new StrategyCity();
            strategyCity.setUpgradeStrategyId(upgradeStrategy.getId());

            String[] pattern = areaId[i].split("#");
            strategyCity.setName(pattern[2]);
            if(pattern[0].equals("root")){
                strategyCity.setProvinceId(pattern[1]);
                strategyCity.setCityId("");
            }else{
                strategyCity.setProvinceId(pattern[0]);
                strategyCity.setCityId(pattern[1]);
                strategyCity.setAreaCode(pattern[3]);
            }
            cityList.add(strategyCity);
        }

        //品牌
        List<StrategyBrandSpec> specList = new ArrayList<>();
        for (int i = 0; i < specificationId.length; i++) {
            StrategyBrandSpec brandSpec = new StrategyBrandSpec();
            brandSpec.setUpgradeStrategyId(upgradeStrategy.getId());

            String[] pattern = specificationId[i].split("#");
            brandSpec.setName(pattern[2]);
            if(pattern[0].equals("root")){//品牌
                brandSpec.setBrandId(pattern[1]);
                brandSpec.setSpecificationId("");
            } else {//型号
                brandSpec.setBrandId(pattern[0]);
                brandSpec.setSpecificationId(pattern[1]);
            }
            specList.add(brandSpec);
        }
        //版本
        upgradeStrategy.setLauncherVersion(launcherVersionService.get(launcherVersionId));

        upgradeStrategyService.saveCascade(upgradeStrategy,cityList,specList,groupList);
        return Result.success();
    }
    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        UpgradeStrategy upgradeStrategy = upgradeStrategyService.get(id);
        model.addAttribute("obj", upgradeStrategy);
        //商户-终端组
        List<StrategyDeviceGroup> groupList = strategyDeviceGroupService.findByUpgradeStrategyId(id);

        //地区
        List<StrategyCity> cityNamesList = strategyCityService.findByUpgradeStrategyId(id);

        //品牌
        List<StrategyBrandSpec> brandList= strategyBrandSpecService.findByUpgradeStrategyId(id);

        model.addAttribute("groupList", groupList);
        model.addAttribute("cityNamesList", cityNamesList);
        model.addAttribute("specNamesList", brandList);
        return "sys/update/strategy/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(UpgradeStrategy upgradeStrategy,String[] tenantId,String[] specificationId,String[] areaId,String launcherVersionId) {
        //商户
//        List<Tenancy> tenancyList = tenancyService.findByIdIn(tenantId);
//        upgradeStrategy.setTenancyList(tenancyList);
        //商户-终端组
        List<StrategyDeviceGroup> groupList = new ArrayList<>();
        if(tenantId.length>0) {
            //商户-终端组
            for(int i=0;i<tenantId.length;i++){
                StrategyDeviceGroup strategyDeviceGroup = new StrategyDeviceGroup();
                strategyDeviceGroup.setUpgradeStrategyId(upgradeStrategy.getId());

                String[] pattern = tenantId[i].split("#");
                strategyDeviceGroup.setName(pattern[2]);
                strategyDeviceGroup.setTenantId(pattern[0]);
                strategyDeviceGroup.setGroupId(pattern[1]);
                groupList.add(strategyDeviceGroup);
            }
        }
        if(tenantId.length==0){//清空信息
            StrategyDeviceGroup strategyDeviceGroup = new StrategyDeviceGroup();//作为删除标识
            strategyDeviceGroup.setTenantId("clearAll");
            groupList.add(strategyDeviceGroup);
        }

        //地区
        List<StrategyCity> cityList = new ArrayList<>();
        if(areaId.length>0 && areaId[0].contains("#")) {
            //地区
            for(int i=0;i<areaId.length;i++){
                StrategyCity strategyCity = new StrategyCity();
                strategyCity.setUpgradeStrategyId(upgradeStrategy.getId());

                String[] pattern = areaId[i].split("#");
                strategyCity.setName(pattern[2]);
                if(pattern[0].equals("root")){
                    strategyCity.setProvinceId(pattern[1]);
                    strategyCity.setCityId("");
                }else{
                    strategyCity.setProvinceId(pattern[0]);
                    strategyCity.setCityId(pattern[1]);
                    strategyCity.setAreaCode(pattern[3]);
                }
                cityList.add(strategyCity);
            }
        }
        if(areaId.length==0){//清空地区信息
            StrategyCity strategyCity = new StrategyCity();//作为删除标识
            strategyCity.setProvinceId("clearAll");
            cityList.add(strategyCity);
        }
        //品牌
        List<StrategyBrandSpec> specList = new ArrayList<>();
        if(specificationId.length>0 && specificationId[0].contains("#")) {
            //品牌
            for (int i = 0; i < specificationId.length; i++) {
                StrategyBrandSpec brandSpec = new StrategyBrandSpec();
                brandSpec.setUpgradeStrategyId(upgradeStrategy.getId());

                String[] pattern = specificationId[i].split("#");
                brandSpec.setName(pattern[2]);
                if(pattern[0].equals("root")){//品牌
                    brandSpec.setBrandId(pattern[1]);
                    brandSpec.setSpecificationId("");
                } else {//型号
                    brandSpec.setBrandId(pattern[0]);
                    brandSpec.setSpecificationId(pattern[1]);
                }
                specList.add(brandSpec);
            }
        }
        if(specificationId.length==0){//清空品牌信息
            StrategyBrandSpec brandSpec = new StrategyBrandSpec();//作为删除标识
            brandSpec.setBrandId("clearAll");
            specList.add(brandSpec);
        }
        //版本
        upgradeStrategy.setLauncherVersion(launcherVersionService.get(launcherVersionId));

        upgradeStrategy.setNewRecord(false);
        upgradeStrategyService.saveCascade(upgradeStrategy,cityList,specList,groupList);
        return Result.success();
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {

        upgradeStrategyService.deleteCascade(Arrays.asList(ids));
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public String detail(@PathVariable("id") String id,Model model) {
        if (!Strings.isBlank(id)) {

            model.addAttribute("obj",  upgradeStrategyService.get(id));
            //商户-终端组
            List<StrategyDeviceGroup> groupList = strategyDeviceGroupService.findByUpgradeStrategyId(id);
            //地区
            List<StrategyCity> cityNamesList = strategyCityService.findByUpgradeStrategyId(id);
            //品牌
            List<StrategyBrandSpec> brandList= strategyBrandSpecService.findByUpgradeStrategyId(id);

            model.addAttribute("groupList", groupList);
            model.addAttribute("cityNamesList", cityNamesList);
            model.addAttribute("specNamesList", brandList);
            return "sys/update/strategy/detail";
        }
        return null;
    }
    @GetMapping("/strategyTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("升级策略树左侧加载")
    public List<Map<String, Object>> strategyTree() {
        List<UpgradeStrategy> list = upgradeStrategyService.findByDelFlag(UpgradeStrategy.DEL_FLAG_NORMAL);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (UpgradeStrategy strategy : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", strategy.getId());
            obj.put("text", strategy.getName());
            tree.add(obj);
        }
        return tree;
    }

    @GetMapping("selectTenant")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择商户")
    public String selectTenant() {
        return "sys/update/strategy/selectTenant";
    }

    @GetMapping("selectDeviceGroup")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择终端组")
    public String selectDeviceGroup(HttpServletRequest req,String tenantId) {
        req.setAttribute("tenantId",tenantId);
        return "sys/update/strategy/selectDeviceGroup";
    }
}
