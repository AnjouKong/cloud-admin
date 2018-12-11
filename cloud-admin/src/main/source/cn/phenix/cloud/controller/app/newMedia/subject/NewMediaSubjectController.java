package cn.phenix.cloud.controller.app.newMedia.subject;

import cn.phenix.cloud.admin.app.newMedia.media.service.NewMediaSeriesService;
import cn.phenix.cloud.admin.app.newMedia.subject.service.NewMediaSubjectDetailService;
import cn.phenix.cloud.admin.app.newMedia.subject.service.NewMediaSubjectService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaSeries;
import cn.phenix.model.app.newMedia.NewMediaSubject;
import cn.phenix.model.app.newMedia.NewMediaSubjectDetail;
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
@Api(tags = "专题管理")
@RequestMapping("platform/newMedia/subject")
public class NewMediaSubjectController extends BaseController {

    @Autowired
    private NewMediaSubjectService mediaSubjectService;
    @Autowired
    private NewMediaSubjectDetailService mediaSubjectDetailService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private NewMediaSeriesService mediaSeriesService;


    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "app/newMedia/subject/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<NewMediaSubject> data(DataTableParameter parameter, NewMediaSubject mediaSubject) {
        return mediaSubjectService.findPage(parameter, mediaSubject);
    }

    @PostMapping("mediaData")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("专题媒资加载")
    public PageTable<NewMediaSubjectDetail> mediaData(DataTableParameter parameter, NewMediaSubjectDetail mediaSubjectDetail) {
        PageTable<NewMediaSubjectDetail> page = mediaSubjectDetailService.findPage(parameter, mediaSubjectDetail);
        for (NewMediaSubjectDetail detail : page.getData()) {
            NewMediaSeries series = mediaSeriesService.get(detail.getMediaSeriesId());
            if (series == null) continue;
            detail.setSubjectId(mediaSubjectDetail.getSubjectId());
            detail.setSeriesName(series.getSeriesName());
            detail.setSeriesCode(series.getSeriesCode());
            detail.setCpName(series.getCpName());
            detail.setReleaseYear(series.getReleaseYear());
        }
        return page;
    }

    @GetMapping("mediaPic")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("跳转专题媒资图片页面")
    public String mediaPic(@Param("subjectId") String subjectId, @Param("detailId") String detailId, Model model) {
        model.addAttribute("detailId", detailId);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("modelName", NewMediaSubjectDetail.modelName);
        return "app/newMedia/subject/mediaPic";
    }

    @PostMapping("mediaPicDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加专题媒资图片")
    public Result mediaPicDo(NewMediaSubjectDetail mediaSubjectDetail, AttMainForm attMainForm) {
        attMainService.updateAttMainForm(attMainForm, mediaSubjectDetail.getId(), NewMediaSubjectDetail.modelName);
        return Result.success();
    }

    @GetMapping("media/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("跳转专题媒资页面")
    public String media(@PathVariable("id") String id, Model model) {
        model.addAttribute("subjectId", id);
        return "app/newMedia/subject/media";
    }

    @GetMapping("selectMedia")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择媒资页面跳转")
    public String selectMedia() {
        return "app/newMedia/subject/selectMedia";
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "app/newMedia/subject/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(NewMediaSubject mediaSubject, AttMainForm attMainForm) {
        // 媒资列表
        List<NewMediaSubjectDetail> mediaList = Lists.newArrayList();
        if (!Strings.isBlank(mediaSubject.getMediaArr())) {
            String mediaJson = mediaSubject.getMediaArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_media = JSONArray.fromObject(mediaJson);
            for (int i = 0; i < jsonArray_media.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_media.get(i);
                NewMediaSubjectDetail mediaSubjectDetail = new NewMediaSubjectDetail();
                mediaSubjectDetail.setMediaSeriesId(map.get("mediaId"));
                mediaSubjectDetail.setLocation(Strings.isBlank(map.get("location")) ? i : Integer.parseInt(map.get("location")));
                mediaList.add(mediaSubjectDetail);
            }
        }
        mediaSubject.setMediaList(mediaList);
        String id = IdGen.uuid().toString();
        mediaSubject.setId(id);
        mediaSubject.setShowBuyButton(Strings.isBlank(mediaSubject.getShowBuyButton()) ? "0" : "1");
        mediaSubjectService.save(mediaSubject);
        attMainService.updateAttMainForm(attMainForm, mediaSubject.getId(), NewMediaSubject.modelName);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        NewMediaSubject mediaSubject = mediaSubjectService.get(id);

        for (NewMediaSubjectDetail detail : mediaSubject.getMediaList()) {
            NewMediaSeries series = mediaSeriesService.get(detail.getMediaSeriesId());
            if (series == null) continue;
            detail.setSeriesName(series.getSeriesName());
            detail.setSeriesCode(series.getSeriesCode());
            detail.setCpName(series.getCpName());
            detail.setReleaseYear(series.getReleaseYear());
        }
        model.addAttribute("obj", mediaSubject);
        model.addAttribute("modelName", NewMediaSubject.modelName);
        return "app/newMedia/subject/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(NewMediaSubject mediaSubject, AttMainForm attMainForm) {
        mediaSubject.getMediaList().clear();
        // 媒资列表
        List<NewMediaSubjectDetail> mediaList = Lists.newArrayList();
        if (!Strings.isBlank(mediaSubject.getMediaArr())) {
            String mediaJson = mediaSubject.getMediaArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_media = JSONArray.fromObject(mediaJson);
            for (int i = 0; i < jsonArray_media.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_media.get(i);
                NewMediaSubjectDetail mediaSubjectDetail = new NewMediaSubjectDetail();
                mediaSubjectDetail.setId(map.get("detailId") == null ? IdGen.uuid().toString() : map.get("detailId"));
                mediaSubjectDetail.setMediaSeriesId(map.get("mediaId"));
                mediaSubjectDetail.setLocation(Strings.isBlank(map.get("location")) ? i : Integer.parseInt(map.get("location")));
                mediaList.add(mediaSubjectDetail);
            }
        }
        mediaSubject.setMediaList(mediaList);
        mediaSubject.setShowBuyButton(Strings.isBlank(mediaSubject.getShowBuyButton()) ? "0" : "1");
        mediaSubjectService.saveOrUpdate(mediaSubject);
        attMainService.updateAttMainForm(attMainForm, mediaSubject.getId(), NewMediaSubject.modelName);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id))
            mediaSubjectService.deleteById(id);
        return Result.success();
    }

    @PostMapping("delete")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除数据")
    public Result deletes(@Param("ids") String ids) {
        mediaSubjectService.deleteByIds(ids.split(","));
        return Result.success();
    }

}
