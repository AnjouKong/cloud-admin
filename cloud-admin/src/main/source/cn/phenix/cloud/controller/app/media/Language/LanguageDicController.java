package cn.phenix.cloud.controller.app.media.Language;

import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.language.LanguageDic;
import cn.phenix.model.sys.SysArea;
import cn.phenix.model.tenant.renter.Tenancy;
import cn.phenix.model.upgrade.StrategyBrandSpec;
import cn.phenix.model.upgrade.StrategyCity;
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
@Api(tags = "字典管理")
@RequestMapping("platform/language/dic")
public class LanguageDicController extends BaseController {

    @Autowired
    private LanguageDicService languageDicService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "app/language/dic/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<LanguageDic> data(LanguageDic languageDic, @Param("draw") Integer draw, DataTableParameter parameter) {
        PageTable<LanguageDic> pageTable = languageDicService.findPage(languageDic, parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(Model model) {
        return "app/language/dic/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(LanguageDic languageDic) {
        List<LanguageDic> list = languageDicService.findByCodeAndDelFlag(languageDic.getCode(),"0");
        if (list.size() > 0) {
            return Result.error("简称已存在");
        }
        languageDicService.save(languageDic);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        LanguageDic languageDic = languageDicService.get(id);
        model.addAttribute("obj", languageDic);
        return "app/language/dic/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(LanguageDic languageDic,String oldCode) {
        languageDic.setNewRecord(false);
        if(!oldCode.equals(languageDic.getCode())){
            List<LanguageDic> list = languageDicService.findByCodeAndDelFlag(languageDic.getCode(),"0");
            if (list.size() > 0) {
                return Result.error("简称已存在");
            }
        }
        languageDicService.save(languageDic);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id))
        languageDicService.deleteById(id);
        return Result.success();
    }

    @PostMapping("deletes")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除数据")
    public Result deletes(String[] ids) {
        languageDicService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public String detail(@PathVariable("id") String id,Model model) {
        LanguageDic languageDic = languageDicService.get(id);
        model.addAttribute("obj", languageDic);
        return "app/language/dic/detail";
    }

    @GetMapping("/applicationTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("升级树加载")
    public List<Map<String, Object>> applicationTree() {
        List<LanguageDic> list = languageDicService.findByDelFlag("0");
        List<Map<String, Object>> tree = new ArrayList<>();
        for (LanguageDic dic : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", dic.getId());
            obj.put("text", dic.getName()+"->"+dic.getCode());
            obj.put("code", dic.getCode());
            tree.add(obj);
        }
        return tree;
    }

}
