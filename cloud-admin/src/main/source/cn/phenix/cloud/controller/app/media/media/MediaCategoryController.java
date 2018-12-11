package cn.phenix.cloud.controller.app.media.media;


import cn.phenix.cloud.admin.app.media.media.service.MediaCategoryService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@Api(tags = "cibn媒资分类")
@RequestMapping("platform/cibn/category")
@RequiresPermissions("sys.manager.unit")
public class MediaCategoryController extends BaseController {
    @Autowired
    private MediaCategoryService mediaCategoryService;

    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index() {
        return "app/media/cibn/category/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation("首页数据加载")
    public PageTable<MediaCategory> data(MediaCategory category, Integer draw, DataTableParameter parameter) {

        PageTable<MediaCategory> pageTable = mediaCategoryService.findPage(parameter,category);
        pageTable.setDraw(draw);

        return pageTable;
    }



    @RequestMapping("add")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "app/media/cibn/category/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(MediaCategory category, HttpServletRequest req) {
            if (category!=null) {
                int u = mediaCategoryService.findByCategoryNameAndDelFlag(category.getCategoryName(),MediaCategory.DEL_FLAG_NORMAL).size();
                if (u != 0)
                    return Result.error("分类名称已存在");
            }
            category.setDelFlag("0");
            category.setCategoryType(1);
            buildModel(category);
            mediaCategoryService.save(category);
            return Result.success();

    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        MediaCategory mediaCategory = mediaCategoryService.get(id);
        model.addAttribute("obj", mediaCategory);
        return "app/media/cibn/category/edit";
    }


    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(MediaCategory mediaCategory) {
        mediaCategoryService.save(mediaCategory);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id))
        mediaCategoryService.deleteById(id);
        return Result.success();
    }

    @PostMapping("delete")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除数据")
    public Result deletes(@Param("ids") String ids) {
        mediaCategoryService.deleteByIds((ids.split(",")));
        return Result.success();
    }

    @GetMapping("detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public String detail(@PathVariable("id") String id, Model model) {
        MediaCategory mediaCategory = mediaCategoryService.get(id);
        model.addAttribute("obj", mediaCategory);
        return "app/media/cibn/category/detail";
    }
}
