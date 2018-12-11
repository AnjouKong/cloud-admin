package cn.phenix.cloud.controller.tenant.device;


import cn.phenix.cloud.admin.tenant.device.service.DeviceUserGroupService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceUserService;
import cn.phenix.cloud.admin.tenant.renter.service.TenancyService;
import cn.phenix.cloud.admin.tenant.renter.service.TenantSceneRecordService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.device.DeviceUser;
import cn.phenix.model.tenant.device.DeviceUserGroup;
import cn.phenix.model.tenant.renter.Tenancy;
import cn.phenix.model.tenant.renter.TenantSceneRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("platform/operate/deviceGroup")
@Api(tags = "终端组管理")
//@RequiresPermissions("sys.manager.unit")
public class DeviceUserGroupController extends BaseController {

    @Autowired
    private DeviceUserGroupService deviceUserGroupService;
    @Autowired
    private TenancyService tenancyService;
    @Autowired
    private TenantSceneRecordService tenantSceneRecordService;
    @Autowired
    private DeviceUserService deviceUserService;


    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index() {
        return "tenant/deviceUserGroup/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    @RequiresPermissions("sys.manager.unit")
    public PageTable<DeviceUserGroup> data(Integer draw, DataTableParameter parameter, DeviceUserGroup deviceUserGroup, String tenancyName) {
        PageTable<DeviceUserGroup> pageTable = deviceUserGroupService.findAllDeviceUserGroupByPage(parameter, deviceUserGroup, tenancyName);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @RequestMapping("sceneRecordData")
    @ResponseBody
    @ApiOperation("首页数据加载")
    @RequiresPermissions("sys.manager.unit")
    public PageTable<TenantSceneRecord> sceneRecordData(Integer draw, DataTableParameter parameter, TenantSceneRecord tenantSceneRecord) {
        PageTable<TenantSceneRecord> pageTable = tenantSceneRecordService.findByGroupId(parameter, tenantSceneRecord);
        pageTable.setDraw(draw);
        return pageTable;
    }


    @ApiOperation("添加页面跳转")
    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add() {
        return "tenant/deviceUserGroup/add";
    }

    @RequestMapping("scene")
    @ApiOperation("场景页面")
    @RequiresPermissions("sys.manager.unit")
    public String scene(String id, String tenantId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("tenantId", tenantId);
        return "tenant/tenant/selectTenantScene";
    }

    @PostMapping(value = "/addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(DeviceUserGroup deviceUserGroup) {
        deviceUserGroupService.save(deviceUserGroup);
        return Result.success();
    }

    @PostMapping(value = "/selectScene")
    @ResponseBody
    @ApiOperation(value = "选择场景")
    @RequiresPermissions("sys.manager.unit")
    public Result selectScene(@Param("id") String id, @Param("sceneId") String sceneId) {
        DeviceUserGroup group = deviceUserGroupService.get(id);
        if (group == null) return Result.error();
        group.setSceneId(sceneId);
        TenantSceneRecord record = new TenantSceneRecord();
        record.setGroupId(id);
        record.setTenantSceneId(sceneId);
        tenantSceneRecordService.save(record);
        deviceUserGroupService.saveOrUpdate(group);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "预览详情")
    public String detail(@PathVariable("id") String id, Model model) {
        if (!Strings.isBlank(id)) {
            DeviceUserGroup user = deviceUserGroupService.get(id);
            if (user != null && StringUtils.isNoneEmpty(user.getTenantId())) {
                Tenancy tenancy = tenancyService.get(user.getTenantId());
                if (tenancy != null && StringUtils.isNoneEmpty(tenancy.getTenancyName())) {
                    user.setTenantName(tenancy.getTenancyName());
                }
            }
            model.addAttribute("obj", user);
            return "tenant/deviceUserGroup/detail";
        }
        return null;
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        List<DeviceUser> list = deviceUserService.findByDeviceGroupCodeAndDelFlag(id);
        if (list != null && list.size() > 0) return Result.error("当前设备组有设备正在使用，请先删除设备。");
        deviceUserGroupService.deleteById(id);
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        for (String id : ids) {
            List<DeviceUser> list = deviceUserService.findByDeviceGroupCodeAndDelFlag(id);
            if (list != null && list.size() > 0) return Result.error("选择设备组有设备正在使用，请先删除设备。");
        }
        deviceUserGroupService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {

        DeviceUserGroup user = deviceUserGroupService.get(id);
        Tenancy tenancy = tenancyService.get(user.getTenantId());
        model.addAttribute("obj1", user);
        model.addAttribute("tenancy", tenancy);
        return "tenant/deviceUserGroup/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(DeviceUserGroup user, String oldId) {
        deviceUserGroupService.saveOrUpdate(user);
        return Result.success();
    }

    @RequestMapping("groupData")
    @ResponseBody
    @ApiOperation("终端组数据加载")
    @RequiresPermissions("sys.manager.user")
    public PageTable<DeviceUserGroup> groupData(Integer draw, DataTableParameter parameter, DeviceUserGroup deviceUserGroup, String tenancyName) {
        PageTable<DeviceUserGroup> pageTable = deviceUserGroupService.findDeviceUserGroupByPage(parameter, deviceUserGroup, tenancyName);
        pageTable.setDraw(draw);

        return pageTable;
    }

    @GetMapping("/groupTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("终端组树加载")
    public List<Map<String, Object>> groupTree() {
        List<DeviceUserGroup> list = deviceUserGroupService.findByDelFlag("0");
        List<Map<String, Object>> tree = new ArrayList<>();
        for (DeviceUserGroup deviceUserGroup : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", deviceUserGroup.getId());
            obj.put("text", deviceUserGroup.getGroupName());
            tree.add(obj);
        }
        return tree;
    }
}
