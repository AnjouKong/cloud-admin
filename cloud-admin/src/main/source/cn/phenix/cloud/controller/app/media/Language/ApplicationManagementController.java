package cn.phenix.cloud.controller.app.media.Language;

import cn.phenix.cloud.admin.app.language.service.ApplicationLanguageService;
import cn.phenix.cloud.admin.app.language.service.ApplicationManagementService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.language.service.ModelManagementService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.language.ApplicationLanguage;
import cn.phenix.model.app.language.ApplicationManagement;
import cn.phenix.model.app.language.LanguageDic;
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
@Api(tags = "应用管理")
@RequestMapping("platform/language/application")
public class ApplicationManagementController extends BaseController {

    @Autowired
    private ApplicationManagementService applicationManagementService;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private ApplicationLanguageService applicationLanguageService;
    @Autowired
    private ModelManagementService modelManagementService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "app/language/application/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<ApplicationManagement> data(ApplicationManagement applicationManagement, @Param("draw") Integer draw, DataTableParameter parameter) {
        PageTable<ApplicationManagement> pageTable = applicationManagementService.findPage(applicationManagement, parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(Model model) {
        List<LanguageDic> list = languageDicService.findByDelFlag("0");
        model.addAttribute("dic",list);
        return "app/language/application/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(ApplicationManagement applicationManagement,String[] dicCodes) {
        if(dicCodes==null||dicCodes.length==0){
            return Result.error("请至少勾选一种语言");
        }
        List<ApplicationManagement> list = applicationManagementService.findByCodeAndDelFlag(applicationManagement.getCode(),"0");
        if (list.size() > 0) {
            return Result.error("版本code已存在");
        }
        //关联表维护
        applicationManagementService.saveCaseCade(applicationManagement,dicCodes);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        ApplicationManagement applicationManagement = applicationManagementService.get(id);
        List<LanguageDic> dicList = languageDicService.findByDelFlag("0");
        List<ApplicationLanguage> selectList = applicationLanguageService.findByDelFlagAndApplicationCode("0",applicationManagement.getCode());
        model.addAttribute("dic",dicList);
        model.addAttribute("selectList",selectList);
        model.addAttribute("obj", applicationManagement);
        return "app/language/application/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(ApplicationManagement applicationManagement,String oldCode,String[] dicCodes) {
        applicationManagement.setNewRecord(false);
        if(dicCodes==null||dicCodes.length==0){
            return Result.error("请至少勾选一种语言");
        }

        //编辑规则：如果code或者语言包改变，则删除模块下所有内容，否则只更改模块基础信息
        boolean delCasecade =false;
        //存储原语言信息的list
        List<ApplicationLanguage> dicList = applicationLanguageService.findByApplicationId(applicationManagement.getId());

        if(!oldCode.equals(applicationManagement.getCode())){
            List<ApplicationManagement> list = applicationManagementService.findByCodeAndDelFlag(applicationManagement.getCode(),"0");
            if (list.size() > 0) {
                return Result.error("版本code已存在");
            }
            delCasecade=true;
        }else{
            //判断新旧词典集是否相同
            if(dicList.size()!= dicCodes.length){
                delCasecade=true;
            }else{
                //心情不好，所以效率什么的 what ever？
                List<String> oldDicCodes = new ArrayList<>();
                for(ApplicationLanguage language:dicList){
                    oldDicCodes.add(language.getLanguageCode());
                }
                for(String newDicCode:dicCodes){
                    if(!oldDicCodes.contains(newDicCode)){
                        delCasecade=true;
                    }
                }
            }
        }
        if(delCasecade){
            //关联表维护
            applicationManagementService.saveCaseCade(applicationManagement,dicCodes);
        }else{
            List<String> idList = new ArrayList<>();
            idList.add(applicationManagement.getId());
            applicationManagement.setModelManagementList(modelManagementService.findByApplicationIdIn(idList));
            applicationManagementService.save(applicationManagement);
        }
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("删除数据")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id))
        applicationManagementService.deleteById(id);
        return Result.success();
    }

    @PostMapping("deletes")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("批量删除数据")
    public Result deletes(String[] ids) {
        applicationManagementService.updateDelFlagCasecade(ids);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public String detail(@PathVariable("id") String id,Model model) {
        ApplicationManagement applicationManagement = applicationManagementService.get(id);
        model.addAttribute("obj", applicationManagement);
        return "app/language/application/detail";
    }

}
