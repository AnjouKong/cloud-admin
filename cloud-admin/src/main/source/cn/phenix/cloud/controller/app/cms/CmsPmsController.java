package cn.phenix.cloud.controller.app.cms;


import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsPmsService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsPms;
import cn.phenix.model.app.cms.CmsResourcesLanguage;
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

@Controller
@RequestMapping("platform/cms/pms")
@Api(tags = "图片管理")
@RequiresPermissions("sys.manager.unit")
public class CmsPmsController extends BaseController {

    @Autowired
    private AttMainService attMainService;
    @Autowired
    private CmsChannelService cmsChannelService;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;
    @Autowired
    private CmsPmsService cmsPmsService;
    @Autowired
    private ServiceBasicInfoService serviceBasicInfoService;

    @RequestMapping("")
    public String index() {
        return "app/cms/pms/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsPms> data(DataTableParameter parameter, CmsPms cmsPms) {
        PageTable<CmsPms> pageTable = cmsPmsService.findPage(parameter, cmsPms);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/pms/add";
    }


    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsPms cmsPms) {
        cmsPmsService.save(cmsPms);
        //语言
        for (String language : cmsPms.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsPms.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.pms);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "配置")
    public String config(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        List<CmsResourcesLanguage> languageList = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.pms);
        model.addAttribute("language", languageList);
        model.addAttribute("modelName", CmsPms.modelName);
        return "app/cms/pms/config";
    }


    @PostMapping(value = "configDo")
    @ResponseBody
    @ApiOperation(value = "配置保存")
    @RequiresPermissions("sys.manager.unit")
    public Result configDo(CmsPms cmsPms, AttMainForm attMainForm) {
        attMainService.updateAttMainForm(attMainForm, cmsPms.getId(), CmsPms.modelName);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsPmsService.get(id));
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("resourceLanguage", cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.pms));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/pms/edit";
    }


    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsPms cmsPms) {
        cmsPmsService.saveOrUpdate(cmsPms);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(cmsPms.getId(), CmsResourcesLanguage.pms);
        //删除无关附件
        for (CmsResourcesLanguage r : list) {
            if (!Arrays.asList(cmsPms.getLanguageDic()).contains(r.getLanguage())) {
                List<AttMain> attMainList = attMainService.getAttMain(cmsPms.getId(), cmsPms.modelName, r.getLanguage());
                for (AttMain a : attMainList) {
                    attMainService.deleteAttMainById(a.getId());
                }
            }
        }
        cmsResourcesLanguageService.deleteResources(cmsPms.getId(), CmsResourcesLanguage.pms);
        //语言
        for (String language : cmsPms.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsPms.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.pms);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsPmsService.deleteById(id);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.pms);
        for (CmsResourcesLanguage r : list) {
            cmsResourcesLanguageService.deleteById(r.getId());
        }
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        cmsPmsService.deleteByIds(ids);
        for (String str : ids) {
            List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(str, CmsResourcesLanguage.pms);
            for (CmsResourcesLanguage r : list) {
                cmsResourcesLanguageService.deleteById(r.getId());
            }
        }
        return Result.success();
    }
}
