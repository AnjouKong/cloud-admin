package cn.phenix.cloud.controller.tenant.device;


import cn.phenix.cloud.admin.tenant.device.service.DeviceVersionService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.device.DeviceVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("platform/operate/version")
@Api(tags = "终端版本管理")
@RequiresPermissions("sys.manager.unit")
public class DeviceVersionController extends BaseController {

    @Autowired
    private DeviceVersionService deviceVersionService;

    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("数据加载")
    @RequiresPermissions("sys.manager.unit")
    public PageTable<DeviceVersion> data(DataTableParameter parameter, DeviceVersion deviceVersion) {
        PageTable<DeviceVersion> pageTable = deviceVersionService.findPage(parameter, deviceVersion);
        return pageTable;
    }

    @RequestMapping("add/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public String add(@PathVariable("id") String id, Model model) {
        model.addAttribute("applicationId", id);
        return "tenant/application/version/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(DeviceVersion deviceVersion, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("tenantId") String tenantId, @Param("standardId") String standardId, @Param("areaId") String areaId) {
        if (DeviceVersion.time.equalsIgnoreCase(deviceVersion.getTactics())) {
            if (Strings.isBlank(startTime) || Strings.isBlank(endTime)) return Result.error("请选择开始时间和结束时间").addCode(1);
            deviceVersion.setTacticsFirstValue(startTime);
            deviceVersion.setTacticsSecondValue(endTime);
        }
        if (DeviceVersion.tenant.equalsIgnoreCase(deviceVersion.getTactics())) {
            if (Strings.isBlank(tenantId)) return Result.error("请选择商户").addCode(1);
            deviceVersion.setTacticsFirstValue(tenantId);
        }
        if (DeviceVersion.lastVersion.equalsIgnoreCase(deviceVersion.getTactics())) {
            if (Strings.isBlank(tenantId)) return Result.error("请选择版本").addCode(1);
            deviceVersion.setTacticsFirstValue(standardId);
        }
        if (DeviceVersion.area.equalsIgnoreCase(deviceVersion.getTactics())) {
            if (Strings.isBlank(areaId)) return Result.error("请选择地区").addCode(1);
            deviceVersion.setTacticsFirstValue(areaId);
        }
        deviceVersionService.save(deviceVersion);
        return Result.success();
    }

    @GetMapping("treeVersion")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("树加载")
    public List<Map<String, Object>> treeTenancy(@Param("id") String id) {
        List<DeviceVersion> list = deviceVersionService.findByApplicationId(id);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (DeviceVersion deviceVersion : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", deviceVersion.getId());
            obj.put("text", deviceVersion.getTacticsName());
            tree.add(obj);
        }
        return tree;
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        deviceVersionService.deleteById(id);
        return Result.success();
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        deviceVersionService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "详情")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", deviceVersionService.get(id));
        return "tenant/application/version/detail";
    }


}
