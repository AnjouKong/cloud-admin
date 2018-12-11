package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysPrivateRouteService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.sys.SysPrivateRoute;
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

/**
 * 私有路由
 *
 **/
@Controller
@Api(tags = "私有路由")
@RequestMapping("platform/sys/privateRoute")
public class PrivateRouteController extends BaseController{

    @Autowired
    private SysPrivateRouteService sysPrivateRouteService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "sys/privateRoute/index";
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "sys/privateRoute/add";
    }

    @GetMapping("addMarry/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加Marry")
    public String addMarry(@PathVariable("id")String id, Model model) {
        model.addAttribute("obj",sysPrivateRouteService.get(id));
        return "sys/privateRoute/addMarry";
    }

    @GetMapping("edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id")String id, Model model) {
        model.addAttribute("obj",sysPrivateRouteService.get(id));
        return "sys/privateRoute/edit";
    }

    @GetMapping("editMarry/{id}&{key}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑Marry")
    public String editMarry(@PathVariable("id")String id,@PathVariable("key")String key, Model model) {
        model.addAttribute("marryUrl",sysPrivateRouteService.get(id).getMarryUrl().get(key));
        model.addAttribute("marryRule",key);
        model.addAttribute("obj",sysPrivateRouteService.get(id));
        return "sys/privateRoute/editMarry";
    }

    @GetMapping("marry/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("Marry页面跳转")
    public String marry(@PathVariable("id")String id, Model model) {
        model.addAttribute("obj",sysPrivateRouteService.get(id));
        return "sys/privateRoute/marry";
    }


    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(SysPrivateRoute route, @Param("marryRule")String marryRule,@Param("marry")String marry) {
        if (sysPrivateRouteService.get(route.getId()) != null)
            return Result.error("系統異常");
        List<Map<String,String>> mapList=new ArrayList<>();
        Map<String,String> map=new HashMap<>();
        map.put(marryRule,marry);
        mapList.add(map);
        route.setMarryUrl(map);
        sysPrivateRouteService.save(route);
        return Result.success();
    }

    @PostMapping("addMarryDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加Marry")
    public Result addMarryDo(@Param("id")String id, @Param("marryRule")String marryRule,@Param("marryUrl")String marryUrl) {
        SysPrivateRoute route=sysPrivateRouteService.get(id);
        route.getMarryUrl().put(marryRule,marryUrl);
        sysPrivateRouteService.saveOrUpdate(route);
        return Result.success();
    }

    @PostMapping("editMarryDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑Marry")
    public Result editMarryDo(@Param("id")String id, @Param("marryRule")String marryRule,@Param("marryUrl")String marryUrl) {
        SysPrivateRoute route=sysPrivateRouteService.get(id);
        route.getMarryUrl().put(marryRule,marryUrl);
        sysPrivateRouteService.saveOrUpdate(route);
        return Result.success();
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(SysPrivateRoute route) {
        SysPrivateRoute sysPrivateRoute=sysPrivateRouteService.get(route.getId());
        sysPrivateRoute.setRouteName(route.getRouteName());

        sysPrivateRouteService.saveOrUpdate(sysPrivateRoute);
        return Result.success();
    }

    @PostMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    @RequiresPermissions("sys.manager.unit")
    public PageTable<SysPrivateRoute> data(DataTableParameter parameter) {
        return sysPrivateRouteService.findPage(parameter);
    }


    @PostMapping("delete/{id}")
    @ResponseBody
    @ApiOperation("删除数据")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        sysPrivateRouteService.deleteById(id);
        return Result.success();
    }

    @PostMapping("deleteMarry")
    @ResponseBody
    @ApiOperation("删除Marry")
    @RequiresPermissions("sys.manager.unit")
    public Result deleteMarry(@Param("id") String id,@Param("key") String key) {
        SysPrivateRoute route=sysPrivateRouteService.get(id);
        route.getMarryUrl().remove(key);
        sysPrivateRouteService.saveOrUpdate(route);
        return Result.success();
    }


}
