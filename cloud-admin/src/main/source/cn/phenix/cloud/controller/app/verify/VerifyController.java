package cn.phenix.cloud.controller.app.verify;

import cn.phenix.cloud.admin.app.media.media.service.MediaCategoryService;
import cn.phenix.cloud.admin.app.verify.service.VerifyService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaCategory;
import cn.phenix.model.app.media.MediaSeries;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mgm
 */
@Controller
@Api(tags = "审核管理")
@RequestMapping("platform/operation/verify")
public class VerifyController extends BaseController {
    @Autowired
    private VerifyService verifyService;
    @Autowired
    private MediaCategoryService mediaCategoryService;


    /**
     * verify审核列表
     *
     * @return
     */

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index(HttpServletRequest req) {
        //分类实时获取
        req.setAttribute("catalog", mediaCategoryService.findByCategoryTypeAndDelFlag(1, MediaCategory.DEL_FLAG_NORMAL));
        return "app/media/verify/index";

    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation("首页数据加载")
    public PageTable<MediaSeries> data(Integer draw, DataTableParameter parameter, String categoryId, String sreleaseYear, String ereleaseYear,String seriesName ) {
        PageTable<MediaSeries> pageTable = verifyService.findPage(parameter, categoryId, sreleaseYear, ereleaseYear,seriesName);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "媒资详情")
    public String detail(@PathVariable("id") String id, Model model) {
        if (!Strings.isBlank(id)) {
            MediaSeries mediaSeries = verifyService.get(id);
            if (!Strings.isBlank(mediaSeries.getCategoryId())) {
                MediaCategory mediaCategory = mediaCategoryService.get(mediaSeries.getCategoryId());
                mediaSeries.setCategoryId(mediaCategory.getCategoryName());//为了详情页面显示
            }
            model.addAttribute("obj", mediaSeries);
            return "app/media/verify/detail";
        }
        return null;
    }

    @PostMapping("/edit")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result edit(@Param("ids") String[] ids, @Param("reason") String reason, @Param("status") String status) {
        verifyService.update(ids, reason, status);
        return Result.success();
    }

}
