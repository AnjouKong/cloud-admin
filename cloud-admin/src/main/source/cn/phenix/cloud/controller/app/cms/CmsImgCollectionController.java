package cn.phenix.cloud.controller.app.cms;


import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsImgCollectionService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsImgCollection;
import cn.phenix.model.app.cms.CmsResourcesLanguage;
import cn.phenix.model.app.language.LanguageDic;
import cn.phenix.model.mechanism.AttMain;
import com.google.common.collect.Lists;
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
@RequestMapping("platform/cms/imgCollection")
@Api(tags = "图片集管理")
@RequiresPermissions("sys.manager.unit")
public class CmsImgCollectionController extends BaseController {

    @Autowired
    private CmsImgCollectionService cmsImgCollectionService;
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
        return "app/cms/imgCollection/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsImgCollection> data(DataTableParameter parameter, CmsImgCollection cmsImgCollection) {
        PageTable<CmsImgCollection> pageTable = cmsImgCollectionService.findPage(parameter, cmsImgCollection);
        for (CmsImgCollection collection : pageTable.getData()) {
            List<AttMain> list = attMainService.getAttMain(collection.getId(), CmsImgCollection.modelName);
            List<String> str = Lists.newArrayList();
            for (AttMain a : list) str.add(a.getFdFilePath());
            collection.setPicUrl(str);
        }
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/imgCollection/add";
    }


    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsImgCollection cmsImgCollection) {
        CmsImgCollection collection = cmsImgCollectionService.save(cmsImgCollection);
        //语言
        for (String language : cmsImgCollection.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsImgCollection.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.imageList);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success().addData(collection);
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String detail(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsImgCollectionService.get(id));
        List<AttMain> list = attMainService.getAttMain(id, CmsImgCollection.modelName);
        model.addAttribute("pic", list);
        return "app/cms/imgCollection/detail";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsImgCollectionService.get(id));
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("resourceLanguage", cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.imageList));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/imgCollection/edit";
    }


    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsImgCollection cmsImgCollection) {
        cmsImgCollectionService.saveOrUpdate(cmsImgCollection);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(cmsImgCollection.getId(), CmsResourcesLanguage.imageList);
        //删除无关附件
        for (CmsResourcesLanguage r : list) {
            if (!Arrays.asList(cmsImgCollection.getLanguageDic()).contains(r.getLanguage())) {
                List<AttMain> attMainList = attMainService.getAttMain(cmsImgCollection.getId(), CmsImgCollection.modelName, r.getLanguage());
                for (AttMain a : attMainList) {
                    attMainService.deleteAttMainById(a.getId());
                }
            }
        }
        cmsResourcesLanguageService.deleteResources(cmsImgCollection.getId(), CmsResourcesLanguage.imageList);
        //语言
        for (String language : cmsImgCollection.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsImgCollection.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.imageList);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "配置")
    public String config(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        List<CmsResourcesLanguage> languageList = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.imageList);
        model.addAttribute("language", languageList);
        model.addAttribute("modelName", CmsImgCollection.modelName);
        return "app/cms/imgCollection/config";
    }

    @PostMapping(value = "configDo")
    @ResponseBody
    @ApiOperation(value = "配置保存")
    @RequiresPermissions("sys.manager.unit")
    public Result configDo(CmsImgCollection cmsImgCollection, AttMainForm attMainForm) {
        attMainService.updateAttMainForm(attMainForm, cmsImgCollection.getId(), CmsImgCollection.modelName);
        return Result.success();
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsImgCollectionService.deleteById(id);
//        attMainService.deleteAttMain(id, CmsImgCollection.modelName);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.imageList);
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
        cmsImgCollectionService.deleteByIds(ids);
        for (String str : ids) {
            //attMainService.deleteAttMain(str, CmsImgCollection.modelName);
            List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(str, CmsResourcesLanguage.image);
            for (CmsResourcesLanguage r : list) {
                cmsResourcesLanguageService.deleteById(r.getId());
            }
        }
        return Result.success();
    }
}
