package cn.phenix.cloud.controller.app.media.Language;

import cn.phenix.cloud.admin.app.language.service.ApplicationManagementService;
import cn.phenix.cloud.admin.app.language.service.FieldManagementService;
import cn.phenix.cloud.admin.app.language.service.ModelManagementService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.language.ModelManagement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@Api(tags = "模块管理")
@RequestMapping("platform/language/model")
public class ModelManagementController extends BaseController {

    @Autowired
    private ApplicationManagementService applicationManagementService;
    @Autowired
    private ModelManagementService modelManagementService;
    @Autowired
    private FieldManagementService fieldManagementService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index(Model model,String applicationId) {
        model.addAttribute("applicationId",applicationId);
        return "app/language/model/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<ModelManagement> data(ModelManagement modelManagement,String applicationId,@Param("draw") Integer draw, DataTableParameter parameter) {
        modelManagement.setApplicationManagement(applicationManagementService.get(applicationId));
        PageTable<ModelManagement> pageTable = modelManagementService.findPage(modelManagement, parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(Model model,String applicationId) {
        model.addAttribute("applicationId",applicationId);
        return "app/language/model/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(ModelManagement modelManagement,String applicationId) {
        List<ModelManagement> list = modelManagementService.findByCodeAndDelFlagAndApplicationId(modelManagement.getCode(),"0",applicationId);
        if (list.size() > 0) {
            return Result.error("模块code已存在，请重新输入");
        }
        modelManagement.setApplicationManagement(applicationManagementService.get(applicationId));
        modelManagementService.save(modelManagement);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model,String applicationId) {
        ModelManagement modelManagement = modelManagementService.get(id);
        model.addAttribute("obj", modelManagement);
        model.addAttribute("applicationId", applicationId);
        return "app/language/model/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(ModelManagement modelManagement,String oldCode,String applicationId) {
        modelManagement.setNewRecord(false);
        boolean delCase=false;
        if(!oldCode.equals(modelManagement.getCode())){
            List<ModelManagement> list = modelManagementService.findByCodeAndDelFlagAndApplicationId(modelManagement.getCode(),"0",applicationId);
            if (list.size() > 0) {
                return Result.error("模块code已存在");
            }
            delCase=true;
        }

        if(delCase){
            //要删除该模块下字段信息
            modelManagementService.saveCaseCade(modelManagement);
        }else {
            modelManagement.setApplicationManagement(applicationManagementService.get(applicationId));
            List<String> modelId =new ArrayList<>();
            modelId.add(modelManagement.getId());
            modelManagement.setFieldManagementList(fieldManagementService.findFieldCodeByModelIdIn(modelId));
            modelManagementService.save(modelManagement);
        }
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id))
        modelManagementService.deleteById(id);
        return Result.success();
    }

    @PostMapping("deletes")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除数据")
    public Result deletes(String[] ids) {
        modelManagementService.updateDelFlagCasecade(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public String detail(@PathVariable("id") String id,Model model) {
        ModelManagement modelManagement = modelManagementService.get(id);
        model.addAttribute("obj", modelManagement);
        return "app/language/model/detail";
    }

}
