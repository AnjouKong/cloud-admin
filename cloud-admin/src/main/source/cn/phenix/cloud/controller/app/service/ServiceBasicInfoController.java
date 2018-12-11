package cn.phenix.cloud.controller.app.service;


import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.service.ServiceBasicInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("platform/service/basic")
@Api(tags = "服务管理")
@RequiresPermissions("sys.manager.unit")
public class ServiceBasicInfoController extends BaseController {

    @Autowired
    private ServiceBasicInfoService serviceBasicInfoService;


    @RequestMapping("")
    @ApiOperation(value = "首页跳转")
    public String index() {
        return "app/service/basic/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "列表查询")
    public PageTable<ServiceBasicInfo> data(DataTableParameter parameter, ServiceBasicInfo serviceBasicInfo) {
        PageTable<ServiceBasicInfo> pageTable = serviceBasicInfoService.findPage(parameter, serviceBasicInfo);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转添加")
    public String add() {
        return "app/service/basic/add";
    }


    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(ServiceBasicInfo serviceBasicInfo) {
        serviceBasicInfoService.save(serviceBasicInfo);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "详情")
    public String detail(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", serviceBasicInfoService.get(id));
        return "app/service/basic/detail";
    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", serviceBasicInfoService.get(id));
        return "app/service/basic/edit";
    }


    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(ServiceBasicInfo serviceBasicInfo) {
        serviceBasicInfoService.saveOrUpdate(serviceBasicInfo);
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        serviceBasicInfoService.deleteById(id);
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        serviceBasicInfoService.deleteByIds(ids);
        return Result.success();
    }
}
