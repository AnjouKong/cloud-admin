package cn.phenix.cloud.controller.app.scene.other;

import cn.phenix.cloud.admin.app.scene.other.service.SceneModelService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.scene.SceneModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Api(tags = "场景模块管理")
@RequestMapping("platform/scene/model")
public class SceneModelController extends BaseController {

    @Autowired
    private SceneModelService sceneModelService;


    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "首页跳转")
    public String index() {
        return "app/scene/model/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation(value = "列表查询")
    public PageTable<SceneModel> data(Integer draw, DataTableParameter parameter, SceneModel sceneModel) {
        PageTable<SceneModel> pageTable = sceneModelService.findPage(parameter, sceneModel);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转添加页面")
    public String add() {
        return "app/scene/model/add";
    }


    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "保存")
    public Result addDo(SceneModel sceneModel) {
        List<SceneModel> sceneModelList = sceneModelService.findByCode(sceneModel.getCode(),null);
        if (sceneModelList != null && sceneModelList.size() > 0) {
            return Result.error("code已重复，请重新输入");
        }
        sceneModelService.save(sceneModel);
        return Result.success();
    }


    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转配置页面")
    public String config(@PathVariable("id") String id, Model model) {
        model.addAttribute("modelId", id);
        return "app/scene/language/index";
    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转编辑页面")
    public String edit(@PathVariable("id") String id, Model model) {
        SceneModel sceneModel = sceneModelService.get(id);
        model.addAttribute("obj", sceneModel);
        return "app/scene/model/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑保存")
    public Result editDo(SceneModel sceneModel) {
        List<SceneModel> sceneModelList = sceneModelService.findByCode(sceneModel.getCode(),sceneModel.getId());
        if (sceneModelList != null && sceneModelList.size() > 0) {
            return Result.error("code已重复，请重新输入");
        }
        if(sceneModel.getId().equals("model_ui")){
            return Result.error("此字典无法修改");
        }
        sceneModelService.saveOrUpdate(sceneModel);
        return Result.success();
    }


    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "删除")
    public Result delete(@PathVariable("id") String id) {
        sceneModelService.deleteById(id);
        return Result.success();
    }

    @PostMapping("deletes")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "批量删除")
    public Result deletes(@Param("ids") String[] ids) {
        sceneModelService.deleteByIds(ids);
        return Result.success();
    }


}
