package cn.phenix.cloud.controller.app.cms;


import cn.phenix.cloud.admin.app.cms.service.CmsAdvertiseService;
import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsAdvertise;
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
@RequestMapping("platform/cms/advertise")
@Api(tags = "广告管理")
@RequiresPermissions("sys.manager.unit")
public class CmsAdvertiseController extends BaseController {

    @Autowired
    private CmsAdvertiseService cmsAdvertiseService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private CmsChannelService cmsChannelService;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;
    @Autowired
    private ServiceBasicInfoService serviceBasicInfoService;

    @RequestMapping("")
    public String index() {
        return "app/cms/advertise/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsAdvertise> data(DataTableParameter parameter, CmsAdvertise cmsAdvertise) {
        PageTable<CmsAdvertise> pageTable = cmsAdvertiseService.findPage(parameter, cmsAdvertise);
//        for(CmsAdvertise img:pageTable.getData()){
//            List<AttMain> mainList = attMainService.getAttMain(img.getId(), CmsAdvertise.modelName);
//            img.setPicUrl(mainList.size() > 0 ? mainList.get(0).getFdFilePath() : null);
//        }
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("service", serviceBasicInfoService.findAll());
        return "app/cms/advertise/add";
    }


    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsAdvertise cmsAdvertise) {
        cmsAdvertiseService.save(cmsAdvertise);
        //语言
        for (String language : cmsAdvertise.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsAdvertise.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.adv);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String detail(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsAdvertiseService.get(id));
        List<AttMain> list = attMainService.getAttMain(id, CmsAdvertise.modelName);
        model.addAttribute("pic", list.size() > 0 ? list.get(0).getFdFilePath() : null);
        return "app/cms/advertise/detail";
    }

    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "配置")
    public String config(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("obj", cmsAdvertiseService.get(id));
        List<CmsResourcesLanguage> languageList = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.adv);
        model.addAttribute("language", languageList);
        model.addAttribute("modelName", CmsAdvertise.modelName);
        return "app/cms/advertise/config";
    }

    @PostMapping(value = "configDo")
    @ResponseBody
    @ApiOperation(value = "配置保存")
    @RequiresPermissions("sys.manager.unit")
    public Result configDo(CmsAdvertise cmsAdvertise, AttMainForm attMainForm) {
        attMainService.updateAttMainForm(attMainForm, cmsAdvertise.getId(), CmsAdvertise.modelName);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsAdvertiseService.get(id));
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("resourceLanguage", cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.adv));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/advertise/edit";
    }


    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsAdvertise cmsAdvertise) {
        cmsAdvertiseService.saveOrUpdate(cmsAdvertise);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(cmsAdvertise.getId(), CmsResourcesLanguage.adv);
        //删除无关附件
        for (CmsResourcesLanguage r : list) {
            if (!Arrays.asList(cmsAdvertise.getLanguageDic()).contains(r.getLanguage())) {
                List<AttMain> attMainList = attMainService.getAttMain(cmsAdvertise.getId(), CmsAdvertise.modelName, r.getLanguage());
                for (AttMain a : attMainList) {
                    attMainService.deleteAttMainById(a.getId());
                }
            }
        }
        cmsResourcesLanguageService.deleteResources(cmsAdvertise.getId(), CmsResourcesLanguage.adv);
        //语言
        for (String language : cmsAdvertise.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsAdvertise.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.adv);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsAdvertiseService.deleteById(id);
        //attMainService.deleteAttMain(id, CmsImg.modelName);.
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.adv);
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
        cmsAdvertiseService.deleteByIds(ids);
        for (String str : ids) {
            //attMainService.deleteAttMain(str, CmsImg.modelName);
            List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(str, CmsResourcesLanguage.adv);
            for (CmsResourcesLanguage r : list) {
                cmsResourcesLanguageService.deleteById(r.getId());
            }
        }
        return Result.success();
    }
}
