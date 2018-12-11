package cn.phenix.cloud.controller.tenant.device;


import cn.phenix.cloud.admin.app.brand.service.BrandService;
import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationFuncService;
import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceUserExtendService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceUserGroupService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceUserService;
import cn.phenix.cloud.admin.tenant.renter.service.TenancyService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.security.Principal;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.brand.BrandSpecification;
import cn.phenix.model.tenant.device.DeviceUser;
import cn.phenix.model.tenant.device.DeviceUserExtends;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("platform/operate/deviceuser")
@Api(tags = "终端管理")
@RequiresPermissions("sys.manager.unit")
public class DeviceUserController extends BaseController {

    @Autowired
    private DeviceUserService deviceUserService;
    @Autowired
    private DeviceUserExtendService deviceUserExtendService;
    @Autowired
    private TenancyService tenancyService;
    @Autowired
    private BrandSpecificationFuncService deviceBrandVersionFuncService;
    @Autowired
    private BrandSpecificationService deviceBrandVersionService;
    @Autowired
    private DeviceUserGroupService deviceUserGroupService;
    @Autowired
    private BrandService brandService;

    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(Model model) {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        Principal principal = (Principal) subject.getPrincipal();
        model.addAttribute("isAdmin", principal.isAdmin());
        return "tenant/deviceUser/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    @RequiresPermissions("sys.manager.unit")
    public PageTable<DeviceUser> data(Integer draw, DataTableParameter parameter, DeviceUser deviceUser) {
        PageTable<DeviceUser> pageTable = deviceUserService.findDeviceUserByPage(parameter, deviceUser);
        pageTable.setDraw(draw);
        for (DeviceUser user : pageTable.getData()) {
            user.setTenantName(tenancyService.get(user.getTenantId()).getTenancyName());
        }
        return pageTable;
    }

    @RequestMapping("testData")
    @ResponseBody
    @ApiOperation("测试组数据加载")
    @RequiresPermissions("sys.manager.unit")
    public PageTable<DeviceUser> testData(DataTableParameter parameter, DeviceUser deviceUser) {
        PageTable<DeviceUser> pageTable = deviceUserService.findForTestGroup(parameter, deviceUser);
        return pageTable;
    }

    @PostMapping(value = "updateTestGroup")
    @ResponseBody
    @ApiOperation(value = "修改测试分组")
    @RequiresPermissions("sys.manager.unit")
    public Result updateTestGroup(@Param("testGroupId") String testGroupId, @Param("ids") String ids) {
        deviceUserService.updateTestGroup(testGroupId, ids.split(","));
        return Result.success();
    }


    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "tenant/deviceUser/add";
    }


    @PostMapping(value = "/addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(DeviceUser deviceUser) {
        if (deviceUser != null) {
            List<DeviceUser> u = deviceUserService.findByDeviceId(deviceUser.getDeviceId());
            if (u.size() > 0)
                return Result.error("设备Id已存在");
        }
        deviceUser.setTenantId("1");

        //有接口后从接口获取
        if (StringUtils.isNotBlank(deviceUser.getLatitude())) {
            deviceUser.setLatitude("1");
        }
        if (StringUtils.isNotBlank(deviceUser.getLongitude())) {
            deviceUser.setLongitude("1");

        }
        deviceUserService.save(deviceUser);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "预览详情")
    public String detail(@PathVariable("id") String id, Model model) {
        if (!Strings.isBlank(id)) {
            DeviceUser user = deviceUserService.get(id);
            DeviceUserExtends deviceUserExtends = deviceUserExtendService.get(id);
            if (deviceUserExtends != null && StringUtils.isNoneBlank(deviceUserExtends.getSpecificationId())) {
                BrandSpecification deviceBrandVersion = deviceBrandVersionService.get(deviceUserExtends.getSpecificationId());
                model.addAttribute("version", deviceBrandVersion);
            }


//            model.addAttribute("funcs",deviceBrandVersionFuncService.findFuncs("1"));
            model.addAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
            model.addAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
            model.addAttribute("extend", deviceUserExtends);
            model.addAttribute("obj", user);

            return "tenant/deviceUser/detail";
        }
        return null;
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id, HttpServletRequest req) {
        DeviceUser user = deviceUserService.get(id);
        deviceUserService.deleteById(id);
        deviceUserExtendService.deleteById(id);
        req.setAttribute("deviceId", user.getDeviceId());
        return Result.success();
    }

    @PostMapping(value = "/removeGroup/{id}")
    @ResponseBody
    @ApiOperation(value = "删除group")
    @RequiresPermissions("sys.manager.unit")
    public Result removeGroup(@PathVariable("id") String id, HttpServletRequest req) {
        deviceUserService.removeGroup(id);
        return Result.success();
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids, HttpServletRequest req) {
        deviceUserService.deleteByIds(ids);
        deviceUserExtendService.deleteByIds(ids);
        req.setAttribute("ids", ids);
        return Result.success("system.success");
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {

        DeviceUser user = deviceUserService.get(id);
        DeviceUserExtends deviceUserExtends = deviceUserExtendService.get(id);
        if (deviceUserExtends != null && StringUtils.isNoneBlank(deviceUserExtends.getSpecificationId())) {
            BrandSpecification deviceBrandVersion = deviceBrandVersionService.get(deviceUserExtends.getSpecificationId());
            if (deviceBrandVersion != null) {
                deviceBrandVersion.setBrandName(brandService.get(deviceBrandVersion.getBrandId()).getBrandName());
            }
            model.addAttribute("version", deviceBrandVersion);
            model.addAttribute("extend", deviceUserExtends);
        }
        if (user.getDeviceGroupCode() != null) {
            if (deviceUserGroupService.get(user.getDeviceGroupCode()) != null) {
                user.setGroupName(deviceUserGroupService.get(user.getDeviceGroupCode()).getGroupName());
            }
        }

//        model.addAttribute("funcs",deviceBrandVersionFuncService.findFuncs("1"));
        model.addAttribute("obj", user);
        return "tenant/deviceUser/edit";
    }

    @GetMapping("/selectDeviceGroup")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String selectDeviceGroup(Model model, String tenantId) {
        model.addAttribute("tenantId", tenantId);
        return "tenant/deviceUser/selectDeviceGroup";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(DeviceUser user, String oldDeviceId, DeviceUserExtends deviceUserExtends) {
        if (!Strings.sBlank(oldDeviceId).equals(user.getDeviceId())) {
            List<DeviceUser> u = deviceUserService.findByDeviceId(user.getDeviceId());
            if (u.size() > 0)
                return Result.error("设备Id已存在");
        }
        //deviceUserService.updateByPrimaryKeySelective(user);
        deviceUserService.updateWithExtends(user, deviceUserExtends);
        return Result.success();
    }

    @RequestMapping("addVersion/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加version")
    public String addVersion(@PathVariable("id") String id, Model model) {
        model.addAttribute("deviceId", id);
        model.addAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
        model.addAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
        return "tenant/deviceUser/addVersion";
    }

    @PostMapping(value = "/addVersionDo")
    @ResponseBody
    @ApiOperation(value = "增加型号功能保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addVersionDo(@Param("standardId") String standardId, @Param("deviceId") String deviceId) {
        DeviceUserExtends deviceUserExtends = new DeviceUserExtends();
        deviceUserExtends.setId(deviceId);
        deviceUserExtends.setSpecificationId(standardId);
        deviceUserExtendService.saveOrUpdate(deviceUserExtends);
        return Result.success();
    }
}
