package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysApiService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.sys.SysApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 应用管理
 *
 * @author xiaobin
 * @create 2017-10-09 下午2:53
 **/
@Controller
@Api(tags = "应用管理")
@RequestMapping("platform/sys/api")
public class ApiController extends BaseController{

    @Autowired
    private SysApiService sysApiService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.api")
    @ApiOperation("首页跳转")
    public String index() {
        return "sys/api/index";
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.api")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "sys/api/add";
    }


    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.api.add")
    @ApiOperation("添加数据")
    public Result addDo(SysApi api) {
        if (sysApiService.get(api.getId()) != null)
            return Result.error("系統異常");
        api.setAppId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10));
        api.setAppSecret(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16));
        buildModel(api);
        sysApiService.save(api);
        return Result.success();
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.api")
    @ApiOperation("首页数据加载")
    public PageTable<SysApi> data(DataTableParameter parameter) {
        return sysApiService.findPage(null, parameter);
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.api")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        sysApiService.deleteById(id);
        return Result.success();
    }

    @ResponseBody
    @RequiresPermissions("sys.manager.api.edit")
    @PostMapping("reset/{id}")
    @ApiOperation("重置appSecret")
    public Result reset(@PathVariable("id") String id) {
        String secret = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        sysApiService.updateByUpdate(Parameter.create().insert("appSecret", secret), Parameter.create().insert("id", id));
        return Result.success();
    }
}
