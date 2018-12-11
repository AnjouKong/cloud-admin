package cn.phenix.cloud.controller.tenant.test;

import cn.phenix.cloud.admin.tenant.test.service.TestSwitchService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.test.TestSwitch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 开关管理
 */
@Controller
@RequestMapping("platform/tenant/switch")
@Api(tags = "开关管理")
@RequiresPermissions("sys.manager.unit")
public class TestSwitchController extends BaseController {
    @Autowired
    private TestSwitchService testSwitchService;


    /**
     * 首页列表
     */
    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index() {
        return "tenant/switch/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation("首页数据加载")
    public PageTable<TestSwitch> data(TestSwitch testSwitch, Integer draw, DataTableParameter parameter) {
        PageTable<TestSwitch> pageTable = testSwitchService.findPage(parameter, testSwitch);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @RequestMapping("add")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "tenant/switch/add";
    }

    @PostMapping(value = "/addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    @Transactional
    public Result addDo(TestSwitch testSwitch) {
        List<TestSwitch> list = testSwitchService.findByCode(testSwitch.getCode());
        if (list != null && list.size() > 0) {
            return Result.error("code已存在");
        }
        testSwitchService.save(testSwitch);
        return Result.success();
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        testSwitchService.deleteById(id);
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        testSwitchService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        TestSwitch testSwitch = testSwitchService.get(id);
        model.addAttribute("obj", testSwitch);
        return "tenant/switch/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(TestSwitch testSwitch) {
        testSwitchService.saveOrUpdate(testSwitch);
        return Result.success();
    }

}
