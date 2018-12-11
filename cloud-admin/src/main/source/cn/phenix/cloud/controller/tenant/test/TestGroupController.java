package cn.phenix.cloud.controller.tenant.test;

import cn.phenix.cloud.admin.tenant.renter.service.TenancyService;
import cn.phenix.cloud.admin.tenant.test.service.TestGroupService;
import cn.phenix.cloud.admin.tenant.test.service.TestGroupSwitchConfigService;
import cn.phenix.cloud.admin.tenant.test.service.TestSwitchService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.test.TestGroup;
import cn.phenix.model.tenant.test.TestGroupSwitchConfig;
import cn.phenix.model.tenant.test.TestSwitch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 测试组管理
 */
@Controller
@RequestMapping("platform/tenant/testGroup")
@Api(tags = "测试组管理")
@RequiresPermissions("sys.manager.unit")
public class TestGroupController extends BaseController {
    @Autowired
    private TestGroupService testGroupService;
    @Autowired
    private TenancyService tenancyService;
    @Autowired
    private TestSwitchService testSwitchService;
    @Autowired
    private TestGroupSwitchConfigService testGroupSwitchConfigService;


    /**
     * 首页列表
     */
    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index() {
        return "tenant/testGroup/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation("首页数据加载")
    public PageTable<TestGroup> data(TestGroup testGroup, Integer draw, DataTableParameter parameter) {
        PageTable<TestGroup> pageTable = testGroupService.findPage(parameter, testGroup);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @RequestMapping("deviceUser/{id}")
    @ApiOperation("终端页面跳转")
    public String deviceUser(@PathVariable("id") String id, Model model) {
        model.addAttribute("testGroupId", id);
        return "tenant/testGroup/deviceUser";
    }

    @RequestMapping("selectDeviceUser/{id}")
    @ApiOperation("选择终端页面跳转")
    public String selectDeviceUser(@PathVariable("id") String id, Model model) {
        model.addAttribute("testGroupId", id);
        model.addAttribute("tenantId", testGroupService.get(id).getTenantId());
        return "tenant/testGroup/selectDeviceUser";
    }

    @RequestMapping("add")
    @ApiOperation("添加页面跳转")
    public String add(Model model) {
        model.addAttribute("testSwitch", testSwitchService.findAll());
        return "tenant/testGroup/add";
    }

    @PostMapping(value = "/addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    @Transactional
    public Result addDo(TestGroup testGroup) {
        String groupId = IdGen.uuid();
        if (!Strings.isBlank(testGroup.getSwitchConfig())) {
            String switchJson = testGroup.getSwitchConfig().replaceAll("&quot;", "\"");
            JSONArray jsonArray_switch = JSONArray.fromObject(switchJson);
            for (int i = 0; i < jsonArray_switch.size(); i++) {
                Map<String, Object> map = (Map<String, Object>) jsonArray_switch.get(i);
                TestGroupSwitchConfig testGroupSwitchConfig = new TestGroupSwitchConfig();
                testGroupSwitchConfig.setGroupId(groupId);
                testGroupSwitchConfig.setSwitchId(map.get("id") + "");
                testGroupSwitchConfig.setSwitchValue(Boolean.parseBoolean(map.get("value") + "") == true ? "1" : "0");
                testGroupSwitchConfigService.save(testGroupSwitchConfig);
            }
        }
        testGroup.setId(groupId);
        testGroupService.save(testGroup);
        return Result.success();
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        testGroupService.deleteById(id);
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        testGroupService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        TestGroup testGroup = testGroupService.get(id);
        model.addAttribute("obj", testGroup);
        model.addAttribute("tenancy", tenancyService.get(testGroup.getTenantId()));
        List<TestSwitch> switchList = testSwitchService.findAll();
        List<TestGroupSwitchConfig> switchConfigList = testGroupSwitchConfigService.findByGroupId(id);
        for (TestSwitch testSwitch : switchList) {
            for (TestGroupSwitchConfig switchConfig : switchConfigList) {
                if (testSwitch.getId().equals(switchConfig.getSwitchId())) {
                    testSwitch.setValue(switchConfig.getSwitchValue());
                }
            }
        }
        model.addAttribute("testSwitch", switchList);
        return "tenant/testGroup/edit";
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "预览详情")
    public String detail(@PathVariable("id") String id, Model model) {
        if (!Strings.isBlank(id)) {
            TestGroup testGroup = testGroupService.get(id);
            testGroup.setTenantName(tenancyService.get(testGroup.getTenantId()).getTenancyName());

            List<TestGroupSwitchConfig> configList = testGroupSwitchConfigService.findByGroupId(id);
            for (TestGroupSwitchConfig switchConfig : configList) {
                switchConfig.setSwitchName(testSwitchService.get(switchConfig.getSwitchId()).getName());
                if("1".equals(switchConfig.getSwitchValue())){
                    switchConfig.setSwitchValue("是");
                }else {
                    switchConfig.setSwitchValue("否");
                }
            }


            model.addAttribute("obj", testGroup);
            model.addAttribute("configList", configList);
            return "tenant/testGroup/detail";
        }
        return null;
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(TestGroup testGroup) {
        if (!Strings.isBlank(testGroup.getSwitchConfig())) {
            testGroupSwitchConfigService.deleteByGroupId(testGroup.getId());
            String switchJson = testGroup.getSwitchConfig().replaceAll("&quot;", "\"");
            JSONArray jsonArray_switch = JSONArray.fromObject(switchJson);
            for (int i = 0; i < jsonArray_switch.size(); i++) {
                Map<String, Object> map = (Map<String, Object>) jsonArray_switch.get(i);
                TestGroupSwitchConfig testGroupSwitchConfig = new TestGroupSwitchConfig();
                testGroupSwitchConfig.setGroupId(testGroup.getId());
                testGroupSwitchConfig.setSwitchId(map.get("id") + "");
                testGroupSwitchConfig.setSwitchValue(Boolean.parseBoolean(map.get("value") + "") == true ? "1" : "0");
                testGroupSwitchConfigService.save(testGroupSwitchConfig);
            }
        }
        testGroupService.saveOrUpdate(testGroup);
        return Result.success();
    }

}
