package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysDictService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.specification.Specifications;
import cn.phenix.model.sys.SysDict;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 字典管理
 *
 * @author xiaobin
 * @create 2017-10-09 下午4:52
 **/
@Controller
@Api(tags = "字典管理")
@RequestMapping("platform/sys/dict")
public class DictController extends BaseController {

    @GetMapping("")
    @ApiOperation("首页跳转")
    public String index(Model model) {
        List<SysDict> dictList = sysDictService.findRootDic("");
        model.addAttribute("obj", dictList);
        return "sys/dict/index";
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.dict")
    @ApiOperation("添加页面跳转")
    public String add(String pid, Model model) {
        if (StringUtils.isBlank(pid))
            pid = SysDict.ROOT_PARENT_ID;
        model.addAttribute("obj", sysDictService.get(pid));
        return "sys/dict/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.dict.add")
    @ApiOperation("添加数据")
    public Result addDo(SysDict dict) {
        List<SysDict> list = sysDictService.findByCode(dict.getCode());
        if (list != null && list.size() > 0) {
            return Result.error("已存在code为" + dict.getCode() + "的字典");
        }
        sysDictService.save(dict);
        return Result.success();
    }

    @PostMapping("child/{id}")
    @RequiresPermissions("sys.manager.dict")
    @ApiOperation("加载数据")
    public String child(@PathVariable("id") String id, Model model) {
        Specification<SysDict> specification = Specifications.<SysDict>and().eq("parent.id", id).eq("delFlag", SysDict.DEL_FLAG_NORMAL).build();
        model.addAttribute("obj", sysDictService.findAll(specification, Sort.by("sort")));
        return "sys/dict/child";
    }

    @GetMapping("edit/{id}")
    @ApiOperation("编辑页面跳转")
    @RequiresPermissions("sys.manager.dict")
    public String edit(@PathVariable("id") String id, Model model) {
        SysDict dict = sysDictService.get(id);
        model.addAttribute("obj", dict);
        if (dict != null) {
            model.addAttribute("parentUnit", dict.getParent());
        }
        return "sys/dict/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @ApiOperation("编辑数据")
    @RequiresPermissions("sys.manager.dict.edit")
    public Result editDo(SysDict dict) {
        List<SysDict> list = sysDictService.findByCode(dict.getCode(), dict.getId());
        if (list != null && list.size() > 0) {
            return Result.error("已存在code为" + dict.getCode() + "的字典");
        }
        SysDict sys = sysDictService.get(dict.getId());
        sys.setName(dict.getName());
        sys.setCode(dict.getCode());
        sysDictService.saveOrUpdate(sys);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @ApiOperation("删除数据")
    @RequiresPermissions("sys.manager.dict.delete")
    public Result delete(@PathVariable("id") String id) {
        sysDictService.deleteById(id);
        return Result.success();
    }

    @GetMapping("tree")
    @ResponseBody
    @ApiOperation("树加载")
    @RequiresPermissions("sys.manager.dict")
    public List<Map<String, Object>> tree(String pid) {
        if (pid == null) {
            pid = "";
        }
        List<SysDict> list = sysDictService.findRootDic(pid);
        List<Map<String, Object>> tree = Lists.newArrayList();
        for (SysDict dict : list) {
            Map<String, Object> obj = Maps.newHashMap();
            obj.put("id", dict.getId());
            obj.put("text", dict.getName());
            obj.put("children", true);
            tree.add(obj);
        }
        return tree;
    }

    @GetMapping("sort")
    @ApiOperation("排序页面跳转")
    @RequiresPermissions("sys.manager.dict")
    public String sort(HttpServletRequest req) {
        List<SysDict> list = sysDictService.findRootDic("");
        req.setAttribute("firstMenus", list);
        return "sys/dict/sort";
    }

    @PostMapping("sortDo")
    @RequiresPermissions("sys.manager.menu.edit")
    @ResponseBody
    @ApiOperation("排序")
    public Result sortDo(String ids) {
        sysDictService.sortDo(ids);
        return Result.success();
    }

    @Autowired
    private SysDictService sysDictService;
}
