package cn.phenix.cloud.controller.app.media.homePage;

import cn.phenix.cloud.admin.app.cms.service.CmsWebsiteService;
import cn.phenix.cloud.admin.app.media.homePage.service.MediaHomePageService;
import cn.phenix.cloud.admin.app.media.homePage.service.MediaHomePageTopService;
import cn.phenix.cloud.admin.app.media.media.service.MediaSeriesService;
import cn.phenix.cloud.admin.app.media.subject.service.MediaSubjectService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
@Api(tags = "媒资首页管理")
@RequestMapping("platform/media/homePage")
public class MediaHomePageController extends BaseController {

    @Autowired
    private MediaHomePageService mediaHomePageService;
    @Autowired
    private MediaHomePageTopService mediaHomePageTopService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private MediaSeriesService mediaSeriesService;
    @Autowired
    private MediaSubjectService mediaSubjectService;



    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "app/media/homePage/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<MediaHomePage> data(DataTableParameter parameter, MediaHomePage mediaHomePage) {
        return mediaHomePageService.findPage(parameter, mediaHomePage);
    }

    @PostMapping("topData")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页媒资加载")
    public PageTable<MediaHomePageTop> topData(DataTableParameter parameter, MediaHomePageTop mediaHomePageTop) {
        PageTable<MediaHomePageTop> page = mediaHomePageTopService.findPage(parameter, mediaHomePageTop);
        for (MediaHomePageTop top : page.getData()) {
            top.setHomePageId(mediaHomePageTop.getHomePageId());
            if (top.getResourceType().equals("detail")) {
                MediaSeries series = mediaSeriesService.get(top.getResourceId());
                if (series != null) {

                }
                top.setName(series.getSeriesName());
            } else if (top.getResourceType().equals("subject")) {
                MediaSubject subject = mediaSubjectService.get(top.getResourceId());
                if (subject == null) continue;
                top.setName(subject.getSubjectName());
            }
        }
        return page;
    }

    @GetMapping("topPic")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("跳转头部图片页面")
    public String topPic(@Param("homePageId") String homePageId, @Param("topId") String topId, Model model) {
        model.addAttribute("topId", topId);
        model.addAttribute("homePageId", homePageId);
        model.addAttribute("modelName", MediaHomePageTop.modelName);
        model.addAttribute("fdk_img", MediaHomePageTop.fdk_img);
        model.addAttribute("fdk_focusImg", MediaHomePageTop.fdk_focusImg);
        return "app/media/homePage/topPic";
    }

    @PostMapping("topPicDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加头部信息图片")
    public Result topPicDo(MediaHomePageTop mediaHomePageTop, AttMainForm attMainForm) {
        attMainService.updateAttMainForm(attMainForm, mediaHomePageTop.getId(), MediaHomePageTop.modelName);
        return Result.success();
    }

    @GetMapping("top/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("跳转首页媒资页面")
    public String media(@PathVariable("id") String id, Model model) {
        model.addAttribute("homePageId", id);
        return "app/media/homePage/top";
    }

    @GetMapping("selectSubject")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择专题页面跳转")
    public String selectSubject() {
        return "app/media/homePage/selectSubject";
    }

    @GetMapping("selectTopMedia")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择头部媒资页面跳转")
    public String selectTopMedia() {
        return "app/media/homePage/selectTopMedia";
    }

    @GetMapping("selectMedia")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择媒资页面跳转")
    public String selectMedia() {
        return "app/media/homePage/selectMedia";
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "app/media/homePage/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(MediaHomePage MediaHomePage) {
        // 头部信息
        List<MediaHomePageTop> topList = Lists.newArrayList();
        if (!Strings.isBlank(MediaHomePage.getSelectedHomePageTopArr())) {
            String topJson = MediaHomePage.getSelectedHomePageTopArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_top = JSONArray.fromObject(topJson);
            for (int i = 0; i < jsonArray_top.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_top.get(i);
                MediaHomePageTop mediaHomePageTop = new MediaHomePageTop();
                mediaHomePageTop.setResourceId(map.get("resourceId"));
                mediaHomePageTop.setResourceType(map.get("resourceType"));
                mediaHomePageTop.setLocation(Strings.isBlank(map.get("location")) ? i : Integer.parseInt(map.get("location")));
                topList.add(mediaHomePageTop);
            }
        }
        MediaHomePage.setTopList(topList);

        // 媒资列表
        List<MediaHomePageDetail> mediaList = Lists.newArrayList();
        if (!Strings.isBlank(MediaHomePage.getMediaArr())) {
            String mediaJson = MediaHomePage.getMediaArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_media = JSONArray.fromObject(mediaJson);
            for (int i = 0; i < jsonArray_media.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_media.get(i);
                MediaHomePageDetail MediaHomePageDetail = new MediaHomePageDetail();
                MediaHomePageDetail.setMediaSeriesId(map.get("mediaId"));
                MediaHomePageDetail.setLocation(Strings.isBlank(map.get("location")) ? i : Integer.parseInt(map.get("location")));
                mediaList.add(MediaHomePageDetail);
            }
        }
        MediaHomePage.setMediaList(mediaList);

        mediaHomePageService.save(MediaHomePage);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        MediaHomePage MediaHomePage = mediaHomePageService.get(id);
        for (MediaHomePageTop top : MediaHomePage.getTopList()) {
            if (top.getResourceType().equals("detail")) {
                MediaSeries series = mediaSeriesService.get(top.getResourceId());
                if (series == null) continue;
                top.setName(series.getSeriesName());
            } else if (top.getResourceType().equals("subject")) {
                MediaSubject subject = mediaSubjectService.get(top.getResourceId());
                if (subject == null) continue;
                top.setName(subject.getSubjectName());
            }

        }
        for (MediaHomePageDetail detail : MediaHomePage.getMediaList()) {
            MediaSeries series = mediaSeriesService.get(detail.getMediaSeriesId());
            if (series == null) continue;
            detail.setSeriesName(series.getSeriesName());
            detail.setSeriesCode(series.getSeriesCode());
            detail.setCpName(series.getCpName());
            detail.setReleaseYear(series.getReleaseYear());
        }
        model.addAttribute("obj", MediaHomePage);
        return "app/media/homePage/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(MediaHomePage mediaHomePage) {
        // 头部信息
        mediaHomePage.getMediaList().clear();
        List<MediaHomePageTop> topList = Lists.newArrayList();
        if (!Strings.isBlank(mediaHomePage.getSelectedHomePageTopArr())) {
            String topJson = mediaHomePage.getSelectedHomePageTopArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_top = JSONArray.fromObject(topJson);
            for (int i = 0; i < jsonArray_top.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_top.get(i);
                MediaHomePageTop mediaHomePageTop = new MediaHomePageTop();
                mediaHomePageTop.setResourceId(map.get("resourceId"));
                mediaHomePageTop.setResourceType(map.get("resourceType"));
                mediaHomePageTop.setId(map.get("topId") == null ? IdGen.uuid().toString() : map.get("topId"));
                mediaHomePageTop.setLocation(Strings.isBlank(map.get("location")) ? i : Integer.parseInt(map.get("location")));
                topList.add(mediaHomePageTop);
            }
        }
        mediaHomePage.setTopList(topList);


        // 媒资列表
        mediaHomePage.getMediaList().clear();
        List<MediaHomePageDetail> mediaList = Lists.newArrayList();
        if (!Strings.isBlank(mediaHomePage.getMediaArr())) {
            String mediaJson = mediaHomePage.getMediaArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_media = JSONArray.fromObject(mediaJson);
            for (int i = 0; i < jsonArray_media.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_media.get(i);
                MediaHomePageDetail MediaHomePageDetail = new MediaHomePageDetail();
                MediaHomePageDetail.setMediaSeriesId(map.get("mediaId"));
                MediaHomePageDetail.setLocation(Strings.isBlank(map.get("location")) ? i : Integer.parseInt(map.get("location")));
                mediaList.add(MediaHomePageDetail);
            }
        }
        mediaHomePage.setMediaList(mediaList);
        mediaHomePageService.saveOrUpdate(mediaHomePage);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id))
            mediaHomePageService.deleteById(id);
        return Result.success();
    }

    @PostMapping("delete")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除数据")
    public Result deletes(@Param("ids") String ids) {
        mediaHomePageService.deleteByIds(ids.split(","));
        return Result.success();
    }

}
