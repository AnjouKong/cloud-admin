package cn.phenix.cloud.controller.app.media.media;


import cn.phenix.cloud.admin.app.media.converge.service.MediaConvergeRuleService;
import cn.phenix.cloud.admin.sys.service.SysDictService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.brand.Brand;
import cn.phenix.model.app.media.MediaConvergeRule;
import cn.phenix.model.sys.SysDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by mgm
 */
@Controller
@Api(tags ="媒资聚合规则")
@RequestMapping("/platform/media/converge/rule")
public class MediaConvergeRuleController extends BaseController {
    @Autowired
    private MediaConvergeRuleService mediaConvergeRuleService;
    @Autowired
    private SysDictService sysDictService;

    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index() {
        return "app/media/converge/rule/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation("首页数据加载")
    public PageTable<MediaConvergeRule> data(MediaConvergeRule mediaConvergeRule, Integer draw, DataTableParameter parameter) {

        PageTable<MediaConvergeRule> pageTable = mediaConvergeRuleService.findPage(parameter,mediaConvergeRule);
        pageTable.setDraw(draw);

        return pageTable;
    }

    @RequestMapping("add")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "app/media/converge/rule/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(MediaConvergeRule mediaConvergeRule, HttpServletRequest req) {
        if (mediaConvergeRule!=null) {
            int u = mediaConvergeRuleService.findByCodeAndDelFlag(mediaConvergeRule.getCode(),MediaConvergeRule.DEL_FLAG_NORMAL).size();
            if (u != 0)
                return Result.error("规则标识已存在");
        }
        mediaConvergeRuleService.save(mediaConvergeRule);
        return Result.success();

    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        MediaConvergeRule mediaConvergeRule = mediaConvergeRuleService.get(id);
        model.addAttribute("obj", mediaConvergeRule);
        return "app/media/converge/rule/edit";
    }


    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(MediaConvergeRule mediaConvergeRule,String oldCode) {
        if (mediaConvergeRule!=null) {
            if (!oldCode.equals(mediaConvergeRule.getCode())) {
                int u = mediaConvergeRuleService.findByCodeAndDelFlag(mediaConvergeRule.getCode(),MediaConvergeRule.DEL_FLAG_NORMAL).size();
                if (u != 0)
                    return Result.error("规则标识已存在");
            }
        }
        mediaConvergeRule.setNewRecord(false);
        mediaConvergeRuleService.save(mediaConvergeRule);
        return Result.success();
    }

    @PostMapping("deletes")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除数据")
    public Result deletes(@Param("ids") String[] ids) {
        mediaConvergeRuleService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public String detail(@PathVariable("id") String id, Model model) {
        MediaConvergeRule mediaConvergeRule = mediaConvergeRuleService.get(id);
        model.addAttribute("obj", mediaConvergeRule);
        return "app/media/converge/rule/detail";
    }


    @GetMapping("/ruleTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public List<Map<String, Object>> ruleTree() {
        List<MediaConvergeRule> list = mediaConvergeRuleService.findByDelFlag(Brand.DEL_FLAG_NORMAL);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (MediaConvergeRule convergeRule : list) {
            Map<String, Object> obj2 = new HashMap<>();
            obj2.put("id", convergeRule.getCode());
            obj2.put("text", convergeRule.getName());
            tree.add(obj2);
        }
        return tree;
    }

    @GetMapping("/convergeTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("媒资转换规则树加载")
    public List<Map<String, Object>> convergeTree() {
        List<SysDict> list =  sysDictService.findByParentId("");
        List<Map<String, Object>> tree = new ArrayList<>();
        for (SysDict sysDict : list) {
            Map<String, Object> obj2 = new HashMap<>();
            obj2.put("id", sysDict.getId());
            obj2.put("text", sysDict.getName());
            obj2.put("code", sysDict.getCode());
            tree.add(obj2);
        }
        return tree;
    }

}
