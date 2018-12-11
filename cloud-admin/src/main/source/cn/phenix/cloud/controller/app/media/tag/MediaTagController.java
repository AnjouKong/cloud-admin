package cn.phenix.cloud.controller.app.media.tag;

import cn.phenix.cloud.admin.app.media.media.service.MediaLevelService;
import cn.phenix.cloud.admin.app.media.subject.service.MediaSubjectService;
import cn.phenix.cloud.admin.app.media.tag.service.MediaTagService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaSubject;
import cn.phenix.model.app.media.MediaTag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@Api(tags = "标签管理")
@RequestMapping("platform/media/tag")
public class MediaTagController extends BaseController {

    @Autowired
    private MediaTagService mediaTagService;

    @Autowired
    private MediaLevelService mediaLevelService;

    @Autowired
    private AttMainService attMainService;

    @Autowired
    private MediaSubjectService mediaSubjectService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "app/media/tag/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<MediaTag> data(@Param("tagName") String tagName, @Param("levelId") String levelId, @Param("draw") Integer draw, DataTableParameter parameter) {
        return mediaTagService.findPage(tagName, levelId, parameter);
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(String levelId, Model model) {
        model.addAttribute("subjectList", mediaSubjectService.findByDelFlag());
        model.addAttribute("level", levelId != null ? mediaLevelService.get(levelId) : mediaLevelService.get("1"));
        return "app/media/tag/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(MediaTag mediaTag, AttMainForm attMainForm) {
        if (mediaTagService.get(mediaTag.getId()) != null)
            return Result.error("系統異常");
        Integer size = mediaTagService.findByTagName(mediaTag.getTagName(), mediaTag.getId(), mediaTag.getLevelId());
        if (size > 0) {
            return Result.error("分类名称已存在");
        }
        String modelId = IdGen.uuid();
        mediaTag.setId(modelId);
        if ("subject".equals(mediaTag.getType())) {
            //设置专题
            if (mediaTag.getSubjectIds() != null && mediaTag.getSubjectIds().length > 0) {
                for (int i = 0; i < mediaTag.getSubjectIds().length; i++) {
                    mediaTag.getMediaSubjectList().add(new MediaSubject(mediaTag.getSubjectIds()[i]));
                }
            }
        }
        mediaTagService.save(mediaTag);
        attMainService.updateAttMainForm(attMainForm, modelId, MediaTag.modelName);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        MediaTag mediaTag = mediaTagService.get(id);
        model.addAttribute("obj", mediaTag);
        model.addAttribute("subjectList", mediaSubjectService.findByDelFlag());
        model.addAttribute("modelName", MediaTag.modelName);
        model.addAttribute("level", mediaTag.getLevelId() != null ? mediaLevelService.get(mediaTag.getLevelId()) : null);
        return "app/media/tag/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(MediaTag mediaTag, AttMainForm attMainForm) {
        Integer size = mediaTagService.findByTagName(mediaTag.getTagName(), mediaTag.getId(), mediaTag.getLevelId());
        if (size > 0) {
            return Result.error("分类名称已存在");
        }
        //设置专题
        mediaTag.getMediaSubjectList().clear();
        if (mediaTag.getSubjectIds() != null && mediaTag.getSubjectIds().length > 0) {
            for (int i = 0; i < mediaTag.getSubjectIds().length; i++) {
                mediaTag.getMediaSubjectList().add(new MediaSubject(mediaTag.getSubjectIds()[i]));
            }
        }
        mediaTagService.saveOrUpdate(mediaTag);
        attMainService.updateAttMainForm(attMainForm, mediaTag.getId(), MediaTag.modelName);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id)) {
            mediaTagService.deleteById(id);
            attMainService.deleteAttMain(id, MediaTag.modelName);
        }
        return Result.success();
    }

    @PostMapping("delete")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除数据")
    public Result deletes(@Param("ids") String ids) {
        mediaTagService.deleteByIds(ids.split(","));
        for (String id : ids.split(",")) {
            attMainService.deleteAttMain(id, MediaTag.modelName);
        }
        return Result.success();
    }

}
