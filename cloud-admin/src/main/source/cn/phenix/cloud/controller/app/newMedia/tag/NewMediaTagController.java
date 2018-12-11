package cn.phenix.cloud.controller.app.newMedia.tag;

import cn.phenix.cloud.admin.app.newMedia.subject.service.NewMediaSubjectService;
import cn.phenix.cloud.admin.app.newMedia.tag.service.NewMediaTagService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaSubject;
import cn.phenix.model.app.newMedia.NewMediaTag;
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
@RequestMapping("platform/newMedia/tag")
public class NewMediaTagController extends BaseController {

    @Autowired
    private NewMediaTagService mediaTagService;

    @Autowired
    private AttMainService attMainService;

    @Autowired
    private NewMediaSubjectService mediaSubjectService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "app/newMedia/tag/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<NewMediaTag> data(@Param("tagName") String tagName, DataTableParameter parameter) {
        return mediaTagService.findPage(tagName, parameter);
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add( Model model) {
        model.addAttribute("subjectList", mediaSubjectService.findByDelFlag());
        return "app/newMedia/tag/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(NewMediaTag mediaTag, AttMainForm attMainForm) {
        if (mediaTagService.get(mediaTag.getId()) != null)
            return Result.error("系統異常");
        Integer size = mediaTagService.findByTagName(mediaTag.getTagName(), mediaTag.getId());
        if (size > 0) {
            return Result.error("分类名称已存在");
        }
        String modelId = IdGen.uuid();
        mediaTag.setId(modelId);
        if ("subject".equals(mediaTag.getType())) {
            //设置专题
            if (mediaTag.getSubjectIds() != null && mediaTag.getSubjectIds().length > 0) {
                for (int i = 0; i < mediaTag.getSubjectIds().length; i++) {
                    mediaTag.getMediaSubjectList().add(new NewMediaSubject(mediaTag.getSubjectIds()[i]));
                }
            }
        }
        mediaTagService.save(mediaTag);
        attMainService.updateAttMainForm(attMainForm, modelId, NewMediaTag.modelName);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        NewMediaTag mediaTag = mediaTagService.get(id);
        model.addAttribute("obj", mediaTag);
        model.addAttribute("subjectList", mediaSubjectService.findByDelFlag());
        model.addAttribute("modelName", NewMediaTag.modelName);
        return "app/newMedia/tag/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(NewMediaTag mediaTag, AttMainForm attMainForm) {
        Integer size = mediaTagService.findByTagName(mediaTag.getTagName(), mediaTag.getId());
        if (size > 0) {
            return Result.error("分类名称已存在");
        }
        //设置专题
        mediaTag.getMediaSubjectList().clear();
        if (mediaTag.getSubjectIds() != null && mediaTag.getSubjectIds().length > 0) {
            for (int i = 0; i < mediaTag.getSubjectIds().length; i++) {
                mediaTag.getMediaSubjectList().add(new NewMediaSubject(mediaTag.getSubjectIds()[i]));
            }
        }
        mediaTagService.saveOrUpdate(mediaTag);
        attMainService.updateAttMainForm(attMainForm, mediaTag.getId(), NewMediaTag.modelName);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id)) {
            mediaTagService.deleteById(id);
            attMainService.deleteAttMain(id, NewMediaTag.modelName);
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
            attMainService.deleteAttMain(id, NewMediaTag.modelName);
        }
        return Result.success();
    }

}
