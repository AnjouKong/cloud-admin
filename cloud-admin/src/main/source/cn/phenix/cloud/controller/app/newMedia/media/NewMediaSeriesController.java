package cn.phenix.cloud.controller.app.newMedia.media;


import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationFuncService;
import cn.phenix.cloud.admin.app.newMedia.converge.service.NewMediaConvergeService;
import cn.phenix.cloud.admin.app.newMedia.ftsearch.service.NewSearchHotMediaService;
import cn.phenix.cloud.admin.app.newMedia.ftsearch.service.NewSearchRecommendMediaService;
import cn.phenix.cloud.admin.app.newMedia.media.service.NewMediaCategoryService;
import cn.phenix.cloud.admin.app.newMedia.media.service.NewMediaSeriesService;
import cn.phenix.cloud.admin.app.newMedia.tag.service.NewMediaTagService;
import cn.phenix.cloud.admin.app.vo.media.NewMediaSeriesTagVo;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaCategory;
import cn.phenix.model.app.newMedia.NewMediaConverge;
import cn.phenix.model.app.newMedia.NewMediaSeries;
import cn.phenix.model.app.newMedia.NewMediaTag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mgm
 */
@Controller
@Api(tags = "本地媒资管理")
@RequestMapping("/platform/newMedia/list")
public class NewMediaSeriesController extends BaseController {
    @Autowired
    private NewMediaSeriesService mediaSeriesService;
    @Autowired
    private NewMediaCategoryService categoryService;
    @Autowired
    private NewMediaTagService mediaTagService;
    @Autowired
    private BrandSpecificationFuncService deviceBrandVersionFuncService;
    @Autowired
    private NewMediaConvergeService mediaConvergeService;
    @Autowired
    private NewSearchHotMediaService newSearchHotMediaService;
    @Autowired
    private NewSearchRecommendMediaService newSearchRecommendMediaService;

    /**
     * media首页列表
     *
     * @return
     */
    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(HttpServletRequest req,
                        String seriesName, String seriesKeyword, String originalCountry, String categoryId, String publish,
                        String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime, String ecreateTime) {
        //全部列表
        req.setAttribute("allCatalog", categoryService.findByDelFlag("0"));
        //影片、型号功能 如4k 蓝光、杜比等
        req.setAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
        req.setAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
        //标签
        req.setAttribute("tag", mediaTagService.findByDelFlagAndType("media"));
        //以下参数为了带条件返回
        req.setAttribute("scharge", scharge);
        req.setAttribute("echarge", echarge);
        req.setAttribute("sreleaseYear", sreleaseYear);
        req.setAttribute("ereleaseYear", ereleaseYear);
        req.setAttribute("ecreateTime", ecreateTime);
        req.setAttribute("screateTime", screateTime);

        req.setAttribute("seriesName", seriesName);
        req.setAttribute("seriesKeyword", seriesKeyword);
        req.setAttribute("originalCountry", originalCountry);
        req.setAttribute("categoryId", categoryId);
        req.setAttribute("publish", publish);

        return "app/newMedia/media/index";

    }

    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    public PageTable<NewMediaSeries> data(@Param("sreleaseYear") String sreleaseYear, @Param("ereleaseYear") String ereleaseYear,
                                          @Param("screateTime") String screateTime, @Param("ecreateTime") String ecreateTime,
                                          @Param("scharge") String scharge, @Param("echarge") String echarge,
                                          NewMediaSeries mediaSeries, Integer draw, DataTableParameter parameter) throws ParseException {
        PageTable<NewMediaSeries> pageTable = mediaSeriesService.findPage(sreleaseYear, ereleaseYear, screateTime, ecreateTime, scharge, echarge, mediaSeries, parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }

    /**
     * 媒资详情
     */

    @GetMapping("/detail")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("预览媒资详情")
    public String detail(String id, Model model, HttpServletRequest req,
                         String seriesName, String seriesKeyword, String originalCountry, String categoryId, String publish,
                         String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime, String ecreateTime) {
        if (!Strings.isBlank(id)) {
            //需要判断不能为空
            NewMediaSeries mediaSeries = mediaSeriesService.get(id);
            if (!Strings.isBlank(mediaSeries.getId())) {
                NewMediaCategory mediaCategory = categoryService.get(mediaSeries.getCategoryId());
                mediaSeries.setCategoryId(mediaCategory.getCategoryName());//为了详情页面显示
            }
            List<NewMediaConverge> convergeList = mediaConvergeService.getListBySeriesId(id);

            model.addAttribute("obj", mediaSeries);
            model.addAttribute("convergeList", convergeList);
            req.setAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
            req.setAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
            //以下参数为了带条件返回
            req.setAttribute("scharge", scharge);
            req.setAttribute("echarge", echarge);
            req.setAttribute("sreleaseYear", sreleaseYear);
            req.setAttribute("ereleaseYear", ereleaseYear);
            req.setAttribute("ecreateTime", ecreateTime);
            req.setAttribute("screateTime", screateTime);

            req.setAttribute("seriesName", seriesName);
            req.setAttribute("seriesKeyword", seriesKeyword);
            req.setAttribute("originalCountry", originalCountry);
            req.setAttribute("categoryId", categoryId);
            req.setAttribute("publish", publish);
        }
        return "app/newMedia/media/detail";
    }

    /**
     * 设置价格
     */

    @GetMapping("/price")
    @ApiOperation(value = "设置价格页面跳转")
    public String price(String id, Model model,
                        String seriesName, String seriesKeyword, String originalCountry, String categoryId, String publish,
                        String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime, String ecreateTime) {

        model.addAttribute("catalog", categoryService.findByDelFlag("0"));
        model.addAttribute("tag", mediaTagService.findByDelFlagAndType("media"));
//        model.addAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
        model.addAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
        NewMediaSeries mediaSeries = mediaSeriesService.get(id);
        if (StringUtils.isNoneBlank(mediaSeries.getFuncIds())) {
            List<String> funcList = Arrays.asList(mediaSeries.getFuncIds().split(","));
            model.addAttribute("funcList", funcList);
        }
        model.addAttribute("obj", mediaSeries);

        //以下参数为了带条件返回
        model.addAttribute("scharge", scharge);
        model.addAttribute("echarge", echarge);
        model.addAttribute("sreleaseYear", sreleaseYear);
        model.addAttribute("ereleaseYear", ereleaseYear);
        model.addAttribute("ecreateTime", ecreateTime);
        model.addAttribute("screateTime", screateTime);
        model.addAttribute("seriesName", seriesName);
        model.addAttribute("seriesKeyword", seriesKeyword);
        model.addAttribute("originalCountry", originalCountry);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("publish", publish);

        return "app/newMedia/media/price";
    }

    /**
     * 修改
     *
     * @return
     */
    @PostMapping("editDo")
    @ResponseBody
    @ApiOperation(value = "设定价格")
    public Result editDo(NewMediaSeries mediaSeries, @Param("upload_Pic_FileName") String upload_Pic_FileName, @Param("mediasTags") String[] mediasTags) {
        mediaSeries.setPublish("0");
        //2018年3月28日10:03:33取消热搜和推荐
        mediaSeries.setIsHotWord("0");
        mediaSeries.setIsRecommend("0");
        newSearchHotMediaService.deleteByMediaId(mediaSeries.getId());
        newSearchRecommendMediaService.deleteByMediaId(mediaSeries.getId());

        if (StringUtils.isNoneBlank(upload_Pic_FileName)) {
            mediaSeries.setPictureUrl(upload_Pic_FileName);
        }
        if (!StringUtils.isNoneBlank(mediaSeries.getFuncIds())) {
            mediaSeries.setFuncIds("");
        }
        List<NewMediaSeriesTagVo> tagNowList = mediaSeriesService.tagNowList(mediaSeries.getId());
        mediaSeriesService.editMediaSeries(mediaSeries, mediasTags);
        mediaSeriesService.updateLocation(tagNowList);
        return Result.success();
    }

    /**
     * 设置价格，多条
     */
    @PostMapping("priceMulti")
    @ResponseBody
    @ApiOperation(value = "批量设定价格")
    public Result priceMulti(@Param("ids") String[] ids, @Param("multiPrice") BigDecimal multiPrice) {
        //更新后的全部内容都在这
        List<NewMediaSeries> mediaSeriesList = new ArrayList<>();
        for (int j = 0; j < ids.length; j++) {
            NewMediaSeries mediaSeries = mediaSeriesService.get(ids[j]);
            mediaSeries.setPublish("0");
            //2018年3月28日10:03:33取消热搜和推荐
            mediaSeries.setIsHotWord("0");
            mediaSeries.setIsRecommend("0");
            newSearchHotMediaService.deleteByMediaId(mediaSeries.getId());
            newSearchRecommendMediaService.deleteByMediaId(mediaSeries.getId());

            mediaSeries.setCharge(multiPrice.multiply(new BigDecimal(100)).intValue());
            mediaSeriesList.add(mediaSeries);
        }
        mediaSeriesService.saveList(mediaSeriesList);
        return Result.success();
    }

    /**
     * 下架
     */
    @RequestMapping("offLine/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("下架媒资")
    public String offLine(@PathVariable("id") String id) {
        //2018年3月28日10:03:33取消热搜和推荐
        NewMediaSeries mediaSeries = mediaSeriesService.get(id);
        mediaSeriesService.updateIsRecommend(id, "0");
        mediaSeriesService.updateIsHotWord(id, "0");
        newSearchHotMediaService.deleteByMediaId(mediaSeries.getId());
        newSearchRecommendMediaService.deleteByMediaId(mediaSeries.getId());

        mediaSeriesService.offLine(id);
        return "redirect:/platform/newMedia/list";
    }

    /**
     * 发布
     */
    @RequestMapping("release/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("发布媒资")
    public String release(@PathVariable("id") String id) {
        //2018年3月28日10:03:33取消热搜和推荐
        NewMediaSeries mediaSeries = mediaSeriesService.get(id);
        mediaSeriesService.updateIsRecommend(id, "0");
        mediaSeriesService.updateIsHotWord(id, "0");
        newSearchHotMediaService.deleteByMediaId(mediaSeries.getId());
        newSearchRecommendMediaService.deleteByMediaId(mediaSeries.getId());

        mediaSeriesService.release(id);
        return "redirect:/platform/newMedia/list";
    }


    @PostMapping("delete")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除媒资")
    public Result deletes(@Param("ids") String ids) {
        mediaSeriesService.delMedias(ids.split(","));

        //2018年3月28日10:10:35 取消热搜和推荐
        for (String id : ids.split(",")) {
            NewMediaSeries mediaSeries = mediaSeriesService.get(id);
            mediaSeriesService.updateIsRecommend(id, "0");
            mediaSeriesService.updateIsHotWord(id, "0");
            newSearchHotMediaService.deleteByMediaId(mediaSeries.getId());
            newSearchRecommendMediaService.deleteByMediaId(mediaSeries.getId());
        }

        return Result.success();
    }


    @RequestMapping("getTags")
    @ResponseBody
    @ApiOperation("获取媒资标签")
    public List<NewMediaTag> getTags() {
        return mediaTagService.findByDelFlagAndType("media");

    }
}
