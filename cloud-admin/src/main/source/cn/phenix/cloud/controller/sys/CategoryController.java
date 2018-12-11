package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysCategoryService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.specification.Specifications;
import cn.phenix.model.sys.SysCategory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 分类管理
 *
 * @author xiaobin
 * @create 2017-10-09 下午4:52
 **/
@Controller
@Api(tags = "分类管理")
@RequestMapping("platform/sys/category")
public class CategoryController extends BaseController {

    @GetMapping("/{modelName}/")
    @ApiOperation("首页跳转")
    public String index(@PathVariable("modelName") String modelName, Model model) {
        List<SysCategory> categoryList = sysCategoryService.findRootDic("", modelName);
        model.addAttribute("obj", categoryList);
        model.addAttribute("modelName", modelName);
        return "sys/category/index";
    }

    @GetMapping("add/{modelName}/")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(@PathVariable("modelName") String modelName, String pid, Model model) {
        if (StringUtils.isBlank(pid)) pid = SysCategory.ROOT_PARENT_ID;
        model.addAttribute("obj", sysCategoryService.get(pid));
        model.addAttribute("modelName", modelName);
        return "sys/category/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(SysCategory category) {
        sysCategoryService.save(category);
        return Result.success();
    }

    @PostMapping("child/{modelName}/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("子分类数据加载")
    public String child(@PathVariable("id") String id, @PathVariable("modelName") String modelName, Model model) {
        Specification<SysCategory> specification = Specifications.<SysCategory>and().eq("parent.id", id).eq("delFlag", SysCategory.DEL_FLAG_NORMAL).build();
        model.addAttribute("obj", sysCategoryService.findAll(specification, Sort.by("sort")));
        model.addAttribute("modelName", modelName);
        return "sys/category/child";
    }

    @GetMapping("edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        SysCategory category = sysCategoryService.get(id);
        model.addAttribute("obj", category);
        if (category != null) {
            model.addAttribute("parentUnit", category.getParent());
        }
        return "sys/category/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(SysCategory category) {
        SysCategory sys = sysCategoryService.get(category.getId());
        sys.setName(category.getName());
        sysCategoryService.saveOrUpdate(sys);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        sysCategoryService.deleteById(id);
        return Result.success();
    }

    @GetMapping("tree/{modelName}/")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("分类树加载")
    public List<Map<String, Object>> tree(@PathVariable("modelName") String modelName, String pid) {
        if (pid == null) {
            pid = "";
        }
        List<SysCategory> list = sysCategoryService.findRootDic(pid, modelName);
        List<Map<String, Object>> tree = Lists.newArrayList();
        for (SysCategory category : list) {
            Map<String, Object> obj = Maps.newHashMap();
            obj.put("id", category.getId());
            obj.put("text", category.getName());
            obj.put("children", true);
            tree.add(obj);
        }
        return tree;
    }

    @GetMapping("categoryTree/{modelName}/")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("分类树加载")
    public List<Map<String, Object>> categoryTree(@PathVariable("modelName") String modelName, String pid) {
        List<Map<String, Object>> tree = Lists.newArrayList();
        Map<String, Object> obj = Maps.newHashMap();
        if (pid == null) {
            pid = "";
            obj.put("id", "root");
            obj.put("text", "所有场景");
            obj.put("children", false);
            tree.add(obj);
        }
        List<SysCategory> list = sysCategoryService.findRootDic(pid, modelName);
        for (SysCategory category : list) {
            obj = Maps.newHashMap();
            obj.put("id", category.getId());
            obj.put("text", category.getName());
            obj.put("children", true);
            tree.add(obj);
        }
        return tree;
    }

    @GetMapping("sort/{modelName}/")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("排序页面跳转")
    public String sort(@PathVariable("modelName") String modelName, Model model) {
        List<SysCategory> list = sysCategoryService.findRootDic("", modelName);
        model.addAttribute("firstMenus", list);
        model.addAttribute("modelName", modelName);
        return "sys/category/sort";
    }

    @PostMapping("sortDo/{modelName}/")
    @RequiresPermissions("sys.manager.unit")
    @ResponseBody
    @ApiOperation("排序")
    public Result sortDo(@PathVariable("modelName") String modelName, String ids) {
        sysCategoryService.sortDo(ids, modelName);
        return Result.success();
    }

    @Autowired
    private SysCategoryService sysCategoryService;
}
