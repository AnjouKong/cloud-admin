package cn.phenix.cloud.controller.app.media.media;


import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationFuncService;
import cn.phenix.cloud.admin.app.media.converge.service.MediaConvergeService;
import cn.phenix.cloud.admin.app.media.ftsearch.service.FtsearchHotwordMediaService;
import cn.phenix.cloud.admin.app.media.ftsearch.service.FtsearchRecommendMediaService;
import cn.phenix.cloud.admin.app.media.media.service.MediaCategoryService;
import cn.phenix.cloud.admin.app.media.media.service.MediaLevelService;
import cn.phenix.cloud.admin.app.media.media.service.MediaSeriesService;
import cn.phenix.cloud.admin.app.media.media.service.ResourceService;
import cn.phenix.cloud.admin.app.media.tag.service.MediaTagService;
import cn.phenix.cloud.admin.app.vo.media.MediaSeriesTagVo;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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
@Api(tags ="本地媒资管理")
@RequestMapping("/platform/media/list")
public class MediaSeriesController extends BaseController {
    @Autowired
    private MediaSeriesService mediaSeriesService;
    @Autowired
    private MediaCategoryService categoryService;
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
    @Autowired
    private FtsearchHotwordMediaService ftsearchHotwordMediaService;
    @Autowired
    private FtsearchRecommendMediaService ftsearchRecommendMediaService;
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

        return "app/media/media/index";

    }

    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    public PageTable<MediaSeries> data(@Param("sreleaseYear") String sreleaseYear,@Param("ereleaseYear") String ereleaseYear,
                                       @Param("screateTime") String screateTime,@Param("ecreateTime") String ecreateTime,
                                       @Param("scharge") String scharge,@Param("echarge") String echarge,
                                       MediaSeries mediaSeries, Integer draw, DataTableParameter parameter) throws ParseException {
        PageTable<MediaSeries> pageTable = mediaSeriesService.findPage(sreleaseYear,ereleaseYear,screateTime,ecreateTime,scharge,echarge,mediaSeries,parameter);

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
        return "app/media/media/detail";
    }

    /**
     * 设置价格
     */

    @GetMapping("/price")
    @ApiOperation(value = "设置价格页面跳转")
    public String price(String id,String levelId,Model model,
                        String seriesName,String seriesKeyword,String originalCountry, String categoryId,String publish,String status,
                        String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime,String ecreateTime) {

        model.addAttribute("catalog",categoryService.findByCategoryTypeAndDelFlag(1,"0"));
        model.addAttribute("tag",mediaTagService.findByDelFlagAndLevelIdAndType(levelId,"media"));
//        model.addAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
        model.addAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
        MediaSeries mediaSeries = mediaSeriesService.get(id);
        if(StringUtils.isNoneBlank(mediaSeries.getFuncIds())){
            List<String> funcList = Arrays.asList(mediaSeries.getFuncIds().split(","));
            model.addAttribute("funcList",funcList);
        }
        model.addAttribute("obj",mediaSeries);

        //以下参数为了带条件返回
        model.addAttribute("scharge",scharge);
        model.addAttribute("echarge",echarge);
        model.addAttribute("sreleaseYear",sreleaseYear);
        model.addAttribute("ereleaseYear",ereleaseYear);
        model.addAttribute("ecreateTime",ecreateTime);
        model.addAttribute("screateTime",screateTime);
        model.addAttribute("seriesName",seriesName);
        model.addAttribute("seriesKeyword",seriesKeyword);
        model.addAttribute("originalCountry",originalCountry);
        model.addAttribute("categoryId",categoryId);
        model.addAttribute("publish",publish);
        model.addAttribute("status",status);

        return "app/media/media/price";
    }

    /**
     * 设定价格
     * @return
     */
    @PostMapping("editDo")
    @ResponseBody
    @ApiOperation(value = "设定价格")
    public Result editDo(MediaSeries mediaSeries,@Param("upload_Pic_FileName") String upload_Pic_FileName,@Param("mediasTags")String[] mediasTags) {
        mediaSeries.setPublish("0");
        mediaSeries.setStatus("3");
        //2018年3月28日10:03:33取消热搜和推荐
        mediaSeries.setIsHotWord("0");
        mediaSeries.setIsRecommend("0");
        ftsearchHotwordMediaService.deleteByLevelIdAndMediaId(mediaSeries.getLevelId(),mediaSeries.getId());
        ftsearchRecommendMediaService.deleteByLevelIdAndMediaId(mediaSeries.getLevelId(),mediaSeries.getId());

        if(StringUtils.isNoneBlank(upload_Pic_FileName)){
            mediaSeries.setPictureUrl(upload_Pic_FileName);
        }
        if(!StringUtils.isNoneBlank(mediaSeries.getFuncIds())){
            mediaSeries.setFuncIds("");
        }
        List<MediaSeriesTagVo> tagNowList = mediaSeriesService.tagNowList(mediaSeries.getId());
        mediaSeriesService.editMediaSeries(mediaSeries,mediasTags);
        mediaSeriesService.updateLocation(tagNowList);
        return Result.success();
    }

    /**
     * 设置价格，多条
     */
    @PostMapping("priceMulti")
    @ResponseBody
    @ApiOperation(value = "批量设定价格")
    public Result priceMulti(@Param("ids") String[] ids, @Param("multiPrice") BigDecimal multiPrice, @Param("score") String score,
                             @Param("multiCategory") String multiCategory, @Param("mediasTags")String[] mediasTags,@Param("funcIds")String funcIds,
                             HttpServletRequest req) {
        //更新后的全部内容都在这
        List<MediaSeries> mediaSeriesList= new ArrayList<>();
        for(int j=0;j<ids.length;j++){
            MediaSeries mediaSeries = mediaSeriesService.get(ids[j]);
            mediaSeries.setPublish("0");
            mediaSeries.setStatus("3");
            //2018年3月28日10:03:33取消热搜和推荐
            mediaSeries.setIsHotWord("0");
            mediaSeries.setIsRecommend("0");
            ftsearchHotwordMediaService.deleteByLevelIdAndMediaId(mediaSeries.getLevelId(),mediaSeries.getId());
            ftsearchRecommendMediaService.deleteByLevelIdAndMediaId(mediaSeries.getLevelId(),mediaSeries.getId());

            mediaSeries.setCharge(multiPrice);
            mediaSeries.setFuncIds(funcIds);
            if(StringUtils.isNotEmpty(score)){
                mediaSeries.setScore(Double.valueOf(score));
            }
            mediaSeries.setCategoryId(multiCategory);
            mediaSeriesList.add(mediaSeries);
        }
        //循环更新

        for(int i = 0;i<mediaSeriesList.size();i++){
            List<MediaSeriesTagVo> tagNowList = mediaSeriesService.tagNowList(mediaSeriesList.get(i).getId());
            mediaSeriesService.editMediaSeries(mediaSeriesList.get(i),mediasTags);
            mediaSeriesService.updateLocation(tagNowList);
        }
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
        MediaSeries mediaSeries=mediaSeriesService.get(id);
        mediaSeriesService.updateIsRecommend(id,"0");
        mediaSeriesService.updateIsHotWord(id,"0");
        ftsearchHotwordMediaService.deleteByLevelIdAndMediaId(mediaSeries.getLevelId(),mediaSeries.getId());
        ftsearchRecommendMediaService.deleteByLevelIdAndMediaId(mediaSeries.getLevelId(),mediaSeries.getId());

        mediaSeriesService.offLine(id);
        return "redirect:/platform/media/list";
    }

    /**
     * 设置级别
     * @return
     */
    @PostMapping("levelMulti")
    @ResponseBody
    @ApiOperation(value = "设定级别")
    public Result levelMulti(@Param("ids")String ids,@Param("levelId")String levelId) {
        int i=0; //跳过条数
        for(String str:ids.split(",")){
            MediaSeries series =  mediaSeriesService.get(str);
            //判断是否在此级别中存在，若存在跳过
            List<MediaSeries> list=mediaSeriesService.findByExtendIdAndLevelId(str,levelId);
            if(list.size()>0){
                i++;
               continue;
            }
            MediaSeries newSeries=new MediaSeries();

            BeanUtils.copyProperties(series,newSeries,"mediaTagsList");
            String newSeriesId =IdGen.uuid();//新的媒资id，在资源resource也要用
            newSeries.setId(newSeriesId);
            newSeries.setFollow("0");
            newSeries.setLevelId(levelId);
            newSeries.setExtendId(str);
            newSeries.setPublish("0");
            newSeries.setStatus("3");
            //2018年3月28日10:03:33取消热搜和推荐
            newSeries.setIsHotWord("0");
            newSeries.setIsRecommend("0");
            //2018年2月9日13:34:13 复制分类
            if(series.getMediaTagsList().size()>0) {
                List<MediaTag> mediaTagsList = series.getMediaTagsList();
                for(MediaTag mediaTag:mediaTagsList){
                    List<MediaTag> tagList = mediaSeriesService.findByDelFlagAndTagNameAndLevelId("0",mediaTag.getTagName(),levelId);
                    if(tagList.size()>0){
                        newSeries.getMediaTagsList().add(new MediaTag(tagList.get(0).getId()));
                    }else{
                        return Result.error("该级别中不存在【"+mediaTag.getTagName()+"】的分类，请添加后再复制！");
                    }
                }
            }

            mediaSeriesService.save(newSeries);
            //str 旧seriesId，newSeriesId新的
            resourceService.copyResources(str,newSeriesId);


        }
        if(i>0){
           MediaLevel level= mediaLevelService.get(levelId);
            return Result.success("["+level.getLevelName()+"]中重复了"+i+"条数据");
        }
        return Result.success();
    }
    @PostMapping("delete")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除媒资")
    public Result deletes(@Param("ids") String ids) {
        mediaSeriesService.delMedias(ids.split(","));

        //2018年3月28日10:10:35 取消热搜和推荐
        for(String id:ids.split(",")){
            MediaSeries mediaSeries=mediaSeriesService.get(id);
            mediaSeriesService.updateIsRecommend(id,"0");
            mediaSeriesService.updateIsHotWord(id,"0");
            ftsearchHotwordMediaService.deleteByLevelIdAndMediaId(mediaSeries.getLevelId(),mediaSeries.getId());
            ftsearchRecommendMediaService.deleteByLevelIdAndMediaId(mediaSeries.getLevelId(),mediaSeries.getId());
        }

        return Result.success();
    }


    @RequestMapping("getTags")
    @ResponseBody
    @ApiOperation("获取媒资标签")
    public List<MediaTag> getTags(HttpServletRequest req,String levelId) {
        return mediaTagService.findByDelFlagAndLevelIdAndType(levelId,"media");

    }
}
