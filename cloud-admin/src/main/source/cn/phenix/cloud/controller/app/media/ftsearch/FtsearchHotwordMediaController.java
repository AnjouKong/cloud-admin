package cn.phenix.cloud.controller.app.media.ftsearch;


import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationFuncService;
import cn.phenix.cloud.admin.app.media.converge.service.MediaConvergeService;
import cn.phenix.cloud.admin.app.media.ftsearch.service.FtsearchHotwordMediaService;
import cn.phenix.cloud.admin.app.media.media.service.MediaCategoryService;
import cn.phenix.cloud.admin.app.media.media.service.MediaLevelService;
import cn.phenix.cloud.admin.app.media.media.service.MediaSeriesService;
import cn.phenix.cloud.admin.app.media.media.service.ResourceService;
import cn.phenix.cloud.admin.app.media.tag.service.MediaTagService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaCategory;
import cn.phenix.model.app.media.MediaConverge;
import cn.phenix.model.app.media.MediaSeries;
import cn.phenix.model.ftsearch.FtsearchHotwordMedia;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/platform/ftsearch/hotWord")
public class FtsearchHotwordMediaController extends BaseController {
    @Autowired
    private FtsearchHotwordMediaService ftsearchHotwordMediaService;

    @Autowired
    private MediaCategoryService categoryService;
    @Autowired
    private MediaSeriesService mediaSeriesService;
    @Autowired
    private MediaTagService mediaTagService;
    @Autowired
    private MediaLevelService mediaLevelService;
    @Autowired
    private BrandSpecificationFuncService deviceBrandVersionFuncService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private MediaConvergeService mediaConvergeService;
    /**
     * media首页列表
     * @return
     */
    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(HttpServletRequest req,String levelId,
                        String seriesName,String seriesKeyword,String originalCountry, String categoryId,String publish,String status,
                        String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime,String ecreateTime) {
        //分类实时获取
        req.setAttribute("catalog",categoryService.findByCategoryTypeAndDelFlag( 1,"0"));
        //全部列表
        req.setAttribute("allCatalog",categoryService.findByDelFlag("0"));
        //影片、型号功能 如4k 蓝光、杜比等
        req.setAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
        req.setAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
        //标签
        req.setAttribute("tag",mediaTagService.findByDelFlagAndLevelIdAndType(StringUtils.isBlank(levelId)?"1":levelId,"media"));
        req.setAttribute("levelId",levelId);
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
        req.setAttribute("status",status);

        return "app/media/ftsearch/hotWord/index";

    }

    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    public PageTable<MediaSeries> data(@Param("sreleaseYear") String sreleaseYear,@Param("ereleaseYear") String ereleaseYear,
                                       @Param("screateTime") String screateTime,@Param("ecreateTime") String ecreateTime,
                                       @Param("scharge") String scharge,@Param("echarge") String echarge,
                                       MediaSeries mediaSeries, Integer draw, DataTableParameter parameter) throws ParseException {
        PageTable<MediaSeries> pageTable = mediaSeriesService.findPublishedPage(sreleaseYear,ereleaseYear,screateTime,ecreateTime,scharge,echarge,mediaSeries,parameter);

        pageTable.setDraw(draw);
        return pageTable;
    }

    /**
     * 媒资详情
     */

    @GetMapping("/detail")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("预览媒资详情")
    public String detail(String id,Model model,HttpServletRequest req,String levelId,
                         String seriesName,String seriesKeyword,String originalCountry, String categoryId,String publish,String status,
                         String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime,String ecreateTime) {
        if (!Strings.isBlank(id)) {
            //需要判断不能为空
            MediaSeries mediaSeries = mediaSeriesService.get(id);
            if(!Strings.isBlank(mediaSeries.getId())){
                MediaCategory mediaCategory = categoryService.get(mediaSeries.getCategoryId());
                mediaSeries.setCategoryId(mediaCategory.getCategoryName());//为了详情页面显示
            }
            List<MediaConverge> convergeList = mediaConvergeService.getListBySeriesId(id);

            model.addAttribute("obj", mediaSeries);
            model.addAttribute("convergeList", convergeList);
            req.setAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
            req.setAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
            //以下参数为了带条件返回
            req.setAttribute("levelId",levelId);
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
            req.setAttribute("status",status);
        }
        return "app/media/ftsearch/hotWord/detail";
    }

    /**
     * 设置热搜
     */
    @PostMapping("setHotWord")
    @ResponseBody
    @ApiOperation(value = "设置热搜")
    public Result setHotWord(String mediaId,String levelId,String isHotWord) {
        if(isHotWord.equals("1")){//设置热搜
            mediaSeriesService.updateIsHotWord(mediaId,isHotWord);

            MediaSeries mediaSeries=mediaSeriesService.get(mediaId);
            FtsearchHotwordMedia hotwordMedia = new FtsearchHotwordMedia();
            hotwordMedia.setMediaSeries(mediaSeries);
            hotwordMedia.setLevelId(levelId);
            ftsearchHotwordMediaService.save(hotwordMedia);
        }else{//取消热搜
            mediaSeriesService.updateIsHotWord(mediaId,isHotWord);
            ftsearchHotwordMediaService.deleteByLevelIdAndMediaId(levelId,mediaId);
        }

        return Result.success();
    }
}
