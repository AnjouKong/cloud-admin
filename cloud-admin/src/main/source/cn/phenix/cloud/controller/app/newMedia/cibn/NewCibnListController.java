package cn.phenix.cloud.controller.app.newMedia.cibn;


import cn.phenix.cloud.admin.app.newMedia.cibn.service.NewCIBNCategoryService;
import cn.phenix.cloud.admin.app.newMedia.cibn.service.NewCIBNListService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cibn.CIBNMediaCategory;
import cn.phenix.model.app.cibn.CIBNMediaSeries;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by fxq
 */
@Controller
@Api(tags = "cibn媒资列表")
@RequestMapping("platform/newCibn/list")
public class NewCibnListController extends BaseController{
    @Autowired
    private NewCIBNListService cibnListService;
    @Autowired
    private NewCIBNCategoryService cibnCategoryService;

    /**
     * cibn首页列表
     * @return
     */
    @RequestMapping("")
    @ApiOperation(value = "首页跳转")
    public String index(HttpServletRequest req) {
        System.out.println("---------CIBN资源库首页--------------");
        List<CIBNMediaCategory> catalog = cibnCategoryService.findByDelFlag();
        req.setAttribute("catalog",catalog);

      return "app/newMedia/cibn/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation(value = "首页数据加载")
    public PageTable<CIBNMediaSeries> data(Integer draw, DataTableParameter parameter,@Param("categoryId") String categoryId,
                                     @Param("sreleaseYear") String sreleaseYear, @Param("ereleaseYear") String ereleaseYear) {

        PageTable<CIBNMediaSeries> pageTable = cibnListService.findPage(parameter,categoryId,sreleaseYear,ereleaseYear);

        pageTable.setDraw(draw);

        return pageTable;

    }
    /**
     * 媒资转换页面跳转
     */
    @RequestMapping("toChange")
    @ApiOperation(value = "媒资转换页面跳转")
    public String toChange(HttpServletRequest req) {
        System.out.println("---------CIBN媒资转换页面--------------");
        List<CIBNMediaCategory> catalog = cibnCategoryService.findByDelFlag();
        req.setAttribute("catalog",catalog);
        return "app/newMedia/cibn/change";

    }
    /**
     * 媒资转换
     */
    @ResponseBody
    @RequestMapping("change")
    @ApiOperation(value = "媒资转换")
    @RequiresPermissions("sys.manager.unit")
    public Result change(@Param("categoryId") String categoryId,
                         @Param("sreleaseYear") String sreleaseYear, @Param("ereleaseYear") String ereleaseYear){
        try {
            cibnListService.change(sreleaseYear,ereleaseYear,categoryId);
            return Result.success("转换成功！");
        } catch (Exception e) {
            logger.error("error",e);
            return Result.error("转换失败！");
        }
    }
    /**
     * 查询媒资转换条数
     */
    @ResponseBody
    @RequestMapping("search")
    @ApiOperation(value = "查询媒资转换条数")
    @RequiresPermissions("sys.manager.unit")
    public Integer search(@Param("categoryId") String categoryId,
                         @Param("sreleaseYear") String sreleaseYear, @Param("ereleaseYear") String ereleaseYear){
        try {
            int num=  cibnListService.search(sreleaseYear,ereleaseYear,categoryId).size();
            return num;
        } catch (Exception e) {
            return 0;
        }
    }
    /**
     * 媒资详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation("查看媒资详情")
    @RequiresPermissions("sys.manager.unit")
    public Object detail(@PathVariable("id")  String id, Model model) {
        if (!Strings.isBlank(id)) {
            CIBNMediaSeries cibnMediaSeries = cibnListService.findById(id);
            model.addAttribute("obj",cibnMediaSeries);
            return "app/newMedia/cibn/detail";
        }
        return null;
    }

}

