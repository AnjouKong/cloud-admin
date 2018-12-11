package cn.phenix.cloud.controller.app.newMedia.search;


import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationFuncService;
import cn.phenix.cloud.admin.app.newMedia.converge.service.NewMediaConvergeService;
import cn.phenix.cloud.admin.app.newMedia.ftsearch.service.NewSearchHotMediaService;
import cn.phenix.cloud.admin.app.newMedia.media.service.NewMediaCategoryService;
import cn.phenix.cloud.admin.app.newMedia.media.service.NewMediaSeriesService;
import cn.phenix.cloud.admin.app.newMedia.tag.service.NewMediaTagService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaCategory;
import cn.phenix.model.app.newMedia.NewMediaConverge;
import cn.phenix.model.app.newMedia.NewMediaSeries;
import cn.phenix.model.newFtsearch.NewSearchHotMedia;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Created by mgm
 */
@Controller
@Api(tags ="热搜管理")
@RequestMapping("/platform/search/media")
public class NewSearchHotMediaController extends BaseController {
    @Autowired
    private NewSearchHotMediaService newSearchHotMediaService;

    @Autowired
    private NewMediaCategoryService categoryService;
    @Autowired
    private NewMediaSeriesService mediaSeriesService;
    @Autowired
    private NewMediaTagService mediaTagService;
    @Autowired
    private BrandSpecificationFuncService deviceBrandVersionFuncService;
    @Autowired
    private NewMediaConvergeService mediaConvergeService;
    /**
     * media首页列表
     * @return
     */
    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(HttpServletRequest req,
                        String seriesName,String seriesKeyword,String originalCountry, String categoryId,String publish,
                        String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime,String ecreateTime) {
        //全部列表
        req.setAttribute("allCatalog",categoryService.findByDelFlag("0"));
        //影片、型号功能 如4k 蓝光、杜比等
        req.setAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
        req.setAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
        //标签
        req.setAttribute("tag",mediaTagService.findByDelFlagAndType("media"));
        //以下参数为了带条件返回
        req.setAttribute("scharge",scharge);
        req.setAttribute("echarge",echarge);
        req.setAttribute("sreleaseYear",sreleaseYear);
        req.setAttribute("ereleaseYear",ereleaseYear);
        req.setAttribute("ecreateTime",ecreateTime);
        req.setAttribute("screateTime",screateTime);

        req.setAttribute("seriesName",seriesName);
        req.setAttribute("seriesKeyword",seriesKeyword);
        req.setAttribute("originalCountry",originalCountry);
        req.setAttribute("categoryId",categoryId);
        req.setAttribute("publish",publish);

        return "app/newMedia/search/hotWord/index";

    }

    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    public PageTable<NewMediaSeries> data(@Param("sreleaseYear") String sreleaseYear,@Param("ereleaseYear") String ereleaseYear,
                                       @Param("screateTime") String screateTime,@Param("ecreateTime") String ecreateTime,
                                       @Param("scharge") String scharge,@Param("echarge") String echarge,
                                       NewMediaSeries mediaSeries, Integer draw, DataTableParameter parameter) throws ParseException {
        PageTable<NewMediaSeries> pageTable = mediaSeriesService.findPublishedPage(sreleaseYear,ereleaseYear,screateTime,ecreateTime,scharge,echarge,mediaSeries,parameter);

        pageTable.setDraw(draw);
        return pageTable;
    }

    /**
     * 媒资详情
     */

    @GetMapping("/detail")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("预览媒资详情")
    public String detail(String id,Model model,HttpServletRequest req,
                         String seriesName,String seriesKeyword,String originalCountry, String categoryId,String publish,
                         String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime,String ecreateTime) {
        if (!Strings.isBlank(id)) {
            //需要判断不能为空
            NewMediaSeries mediaSeries = mediaSeriesService.get(id);
            if(!Strings.isBlank(mediaSeries.getId())){
                NewMediaCategory mediaCategory = categoryService.get(mediaSeries.getCategoryId());
                mediaSeries.setCategoryId(mediaCategory.getCategoryName());//为了详情页面显示
            }
            List<NewMediaConverge> convergeList = mediaConvergeService.getListBySeriesId(id);

            model.addAttribute("obj", mediaSeries);
            model.addAttribute("convergeList", convergeList);
            req.setAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
            req.setAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
            //以下参数为了带条件返回
            req.setAttribute("scharge",scharge);
            req.setAttribute("echarge",echarge);
            req.setAttribute("sreleaseYear",sreleaseYear);
            req.setAttribute("ereleaseYear",ereleaseYear);
            req.setAttribute("ecreateTime",ecreateTime);
            req.setAttribute("screateTime",screateTime);

            req.setAttribute("seriesName",seriesName);
            req.setAttribute("seriesKeyword",seriesKeyword);
            req.setAttribute("originalCountry",originalCountry);
            req.setAttribute("categoryId",categoryId);
            req.setAttribute("publish",publish);
        }
        return "app/newMedia/search/hotWord/detail";
    }

    /**
     * 设置热搜
     */
    @PostMapping("setHotWord")
    @ResponseBody
    @ApiOperation(value = "设置热搜")
    public Result setHotWord(String mediaId,String isHotWord) {
        if(isHotWord.equals("1")){//设置热搜
            mediaSeriesService.updateIsHotWord(mediaId,isHotWord);

            NewMediaSeries mediaSeries=mediaSeriesService.get(mediaId);
            NewSearchHotMedia hotwordMedia = new NewSearchHotMedia();
            hotwordMedia.setMediaSeries(mediaSeries);
            newSearchHotMediaService.save(hotwordMedia);
        }else{//取消热搜
            mediaSeriesService.updateIsHotWord(mediaId,isHotWord);
            newSearchHotMediaService.deleteByMediaId(mediaId);
        }

        return Result.success();
    }
}
