package cn.phenix.cloud.controller.app.media.Language;


import cn.phenix.cloud.admin.app.language.service.*;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.language.ApplicationLanguage;
import cn.phenix.model.app.language.FieldLanguage;
import cn.phenix.model.app.language.FieldManagement;
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
@Api(tags = "字段管理")
@RequestMapping("platform/language/field")
public class FieldManagementController extends BaseController {

    @Autowired
    private FieldManagementService fieldManagementService;
    @Autowired
    private ModelManagementService modelManagementService;
    @Autowired
    private ApplicationLanguageService applicationLanguageService;
    @Autowired
    private ApplicationManagementService applicationManagementService;
    @Autowired
    private FieldLanguageService fieldLanguageService;


    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index(Model model,String modelId,String applicationId) {
        model.addAttribute("applicationId",applicationId);
        model.addAttribute("modelId",modelId);
        return "app/language/field/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable< FieldManagement> data(FieldManagement  fieldManagement, String modelId,@Param("draw") Integer draw, DataTableParameter parameter) {
        fieldManagement.setModelManagement(modelManagementService.get(modelId));
        PageTable< FieldManagement> pageTable =  fieldManagementService.findPage( fieldManagement, parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(Model model, String modelId,String applicationId) {

        List<ApplicationLanguage> selectList = applicationLanguageService.findByDelFlagAndApplicationCode("0",applicationManagementService.get(applicationId).getCode());

        model.addAttribute("selectList",selectList);
        model.addAttribute("applicationId",applicationId);
        model.addAttribute("modelId",modelId);
        return "app/language/field/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(FieldManagement  fieldManagement, String modelId,String[] languageCode,String[] value) {
        fieldManagement.setModelManagement(modelManagementService.get(modelId));
        List< FieldManagement> list =  fieldManagementService.findByCodeAndDelFlagAndModelId( fieldManagement.getCode(),"0",modelId);
        if (list.size() > 0) {
            return Result.error("简称已存在");
        }
        List<FieldLanguage> fieldLanguageList = new ArrayList<>();
        for(int i=0;i<languageCode.length;i++){
            FieldLanguage fieldLanguage = new FieldLanguage();
            fieldLanguage.setFieldCode(fieldManagement.getCode());
            fieldLanguage.setLanguageCode(languageCode[i]);
            fieldLanguage.setValue(value[i]);
            fieldLanguage.setModelId(modelId);
            fieldLanguage.setFieldId(fieldManagement.getId());
            fieldLanguageList.add(fieldLanguage);
        }

        fieldManagementService.saveCaseCade( fieldManagement,fieldLanguageList);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model,String modelId,String applicationId) {
        FieldManagement  fieldManagement =  fieldManagementService.get(id);
        List<FieldLanguage> selectList = fieldLanguageService.findByDelFlagAndFieldCodeAndModelId("0",fieldManagement.getCode(),modelId);

        model.addAttribute("selectList",selectList);
        model.addAttribute("obj",  fieldManagement);
        model.addAttribute("modelId",  modelId);
        model.addAttribute("applicationId",  applicationId);
        return "app/language/field/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(FieldManagement  fieldManagement,String oldCode,String modelId,String[] languageCode,String[] value) {
        fieldManagement.setNewRecord(false);
        fieldManagement.setModelManagement(modelManagementService.get(modelId));
        if(!oldCode.equals( fieldManagement.getCode())){
            List<FieldManagement> list =  fieldManagementService.findByCodeAndDelFlagAndModelId( fieldManagement.getCode(),"0",modelId);
            if (list.size() > 0) {
                return Result.error("简称已存在");
            }
        }
        List<FieldLanguage> fieldLanguageList = new ArrayList<>();
        for(int i=0;i<languageCode.length;i++){
            FieldLanguage fieldLanguage = new FieldLanguage();
            fieldLanguage.setFieldCode(fieldManagement.getCode());
            fieldLanguage.setLanguageCode(languageCode[i]);
            fieldLanguage.setValue(value[i]);
            fieldLanguage.setModelId(modelId);
            fieldLanguage.setFieldId(fieldManagement.getId());

            fieldLanguageList.add(fieldLanguage);
        }

        fieldManagementService.saveCaseCade( fieldManagement,fieldLanguageList);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id))
         fieldManagementService.deleteById(id);
        return Result.success();
    }

    @PostMapping("deletes")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除数据")
    public Result deletes(String[] ids,String modelId) {
        fieldManagementService.updateDelFlagCasecade(ids,modelId);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public String detail(@PathVariable("id") String id,Model model,String modelId) {
        FieldManagement  fieldManagement =  fieldManagementService.get(id);
        List<FieldLanguage> selectList = fieldLanguageService.findByDelFlagAndFieldCodeAndModelId("0",fieldManagement.getCode(),modelId);

        model.addAttribute("selectList",selectList);
        model.addAttribute("obj",  fieldManagement);
        return "app/language/field/detail";
    }

}
