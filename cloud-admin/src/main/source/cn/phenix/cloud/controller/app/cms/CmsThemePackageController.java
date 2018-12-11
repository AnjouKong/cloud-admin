
package cn.phenix.cloud.controller.app.cms;

import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.cms.service.CmsThemePackageService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsResourcesLanguage;
import cn.phenix.model.app.cms.CmsThemePackage;
import cn.phenix.model.app.language.LanguageDic;
import cn.phenix.model.mechanism.AttMain;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * Created by dwp on 2018/9/27.
 */

@Controller
@RequestMapping("platform/cms/theme")
@Api(tags = "主题包管理")
@RequiresPermissions("sys.manager.unit")
public class CmsThemePackageController {

    @Autowired
    private AttMainService attMainService;
    @Autowired
    private CmsChannelService cmsChannelService;

    @Autowired
    private ServiceBasicInfoService serviceBasicInfoService;
    @Autowired
    private CmsThemePackageService cmsThemePackageService;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;

    @RequestMapping("")
    public String index() {
        return "app/cms/theme/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsThemePackage> data(DataTableParameter parameter, CmsThemePackage cmsThemePackage) {
        PageTable<CmsThemePackage> pageTable = cmsThemePackageService.findPage(parameter, cmsThemePackage);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("service", serviceBasicInfoService.findAll());
        return "app/cms/theme/add";
    }

    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsThemePackage cmsThemePackage) {
        cmsThemePackageService.save(cmsThemePackage);
        //语言
        for (String language : cmsThemePackage.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsThemePackage.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.theme);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "配置")
    public String config(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        List<CmsResourcesLanguage> languageList = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.theme);
        model.addAttribute("language", languageList);
        model.addAttribute("modelName", CmsThemePackage.modelName);

        return "app/cms/theme/config";
    }

    @PostMapping(value = "configDo")
    @ResponseBody
    @ApiOperation(value = "配置保存")
    @RequiresPermissions("sys.manager.unit")
    public Result configDo(CmsThemePackage cmsThemePackage, AttMainForm attMainForm) {
        attMainService.updateAttMainForm(attMainForm, cmsThemePackage.getId(), cmsThemePackage.modelName);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsThemePackageService.get(id));
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("resourceLanguage", cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.theme));
        model.addAttribute("service", serviceBasicInfoService.findAll());
        return "app/cms/theme/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsThemePackage cmsThemePackage) {
        cmsThemePackageService.saveOrUpdate(cmsThemePackage);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(cmsThemePackage.getId(), CmsResourcesLanguage.theme);
        //删除无关附件
        for (CmsResourcesLanguage r : list) {
            if (!Arrays.asList(cmsThemePackage.getLanguageDic()).contains(r.getLanguage())) {
                List<AttMain> attMainList = attMainService.getAttMain(cmsThemePackage.getId(), CmsThemePackage.modelName, r.getLanguage());
                for (AttMain a : attMainList) {
                    attMainService.deleteAttMainById(a.getId());
                }
            }
        }
        cmsResourcesLanguageService.deleteResources(cmsThemePackage.getId(), CmsResourcesLanguage.theme);
        //语言
        for (String language : cmsThemePackage.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsThemePackage.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.theme);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsThemePackageService.deleteById(id);
        return Result.success();
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        cmsThemePackageService.deleteByIds(ids);
        for (String str : ids) {
            //attMainService.deleteAttMain(str, CmsImg.modelName);
            List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(str, CmsResourcesLanguage.theme);
            for (CmsResourcesLanguage r : list) {
                cmsResourcesLanguageService.deleteById(r.getId());
            }
        }
        return Result.success();
    }
}

