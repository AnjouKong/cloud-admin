package cn.phenix.cloud.controller.app.cms;


import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.cms.service.CmsVideoService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsResourcesLanguage;
import cn.phenix.model.app.cms.CmsVideo;
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
@RequestMapping("platform/cms/video")
@Api(tags = "视频管理")
@RequiresPermissions("sys.manager.unit")
public class CmsVideoController extends BaseController {

    @Autowired
    private CmsVideoService cmsVideoService;
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
        return "app/cms/video/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsVideo> data(DataTableParameter parameter, CmsVideo cmsVideo) {
        PageTable<CmsVideo> pageTable = cmsVideoService.findPage(parameter, cmsVideo);
//        for(CmsVideo img:pageTable.getData()){
//            List<AttMain> mainList = attMainService.getAttMain(img.getId(), CmsVideo.modelName);
//            List<String> str = Lists.newArrayList();
//            for (AttMain a : mainList) str.add(a.getFdFilePath());
//            img.setPicUrl(str);
//
//        }
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/video/add";
    }


    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsVideo cmsVideo) {
        CmsVideo img = cmsVideoService.save(cmsVideo);
        //语言
        for (String language : cmsVideo.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsVideo.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.video);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success().addData(img);
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String detail(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsVideoService.get(id));
        List<AttMain> list = attMainService.getAttMain(id, CmsVideo.modelName);
        model.addAttribute("pic", list.size() > 0 ? list.get(0).getFdFilePath() : null);
        return "app/cms/video/detail";
    }

    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "配置")
    public String config(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        List<CmsResourcesLanguage> languageList = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.video);
        model.addAttribute("language", languageList);
        model.addAttribute("modelName", CmsVideo.modelName);
        return "app/cms/video/config";
    }

    @PostMapping(value = "configDo")
    @ResponseBody
    @ApiOperation(value = "配置保存")
    @RequiresPermissions("sys.manager.unit")
    public Result configDo(CmsVideo cmsVideo, AttMainForm attMainForm) {
        attMainService.updateAttMainForm(attMainForm, cmsVideo.getId(), CmsVideo.modelName);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsVideoService.get(id));
        model.addAttribute("language",languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("resourceLanguage", cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.video));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/video/edit";
    }


    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsVideo cmsVideo) {
        cmsVideoService.saveOrUpdate(cmsVideo);
        List<CmsResourcesLanguage> list=cmsResourcesLanguageService.findResources(cmsVideo.getId(), CmsResourcesLanguage.video);
        //删除无关附件
        for(CmsResourcesLanguage r:list){
            if(!Arrays.asList(cmsVideo.getLanguageDic()).contains(r.getLanguage())){
                List<AttMain> attMainList = attMainService.getAttMain(cmsVideo.getId(), CmsVideo.modelName, r.getLanguage());
                for(AttMain a:attMainList){
                    attMainService.deleteAttMainById(a.getId());
                }
            }
        }
        cmsResourcesLanguageService.deleteResources(cmsVideo.getId(), CmsResourcesLanguage.video);
        //语言
        for (String language : cmsVideo.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsVideo.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.video);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsVideoService.deleteById(id);
//        attMainService.deleteAttMain(id, CmsImg.modelName);
        List<CmsResourcesLanguage> list=cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.video);
        for(CmsResourcesLanguage r:list){
            cmsResourcesLanguageService.deleteById(r.getId());
        }
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        cmsVideoService.deleteByIds(ids);
        for (String str : ids) {
            //attMainService.deleteAttMain(str, CmsImg.modelName);
            List<CmsResourcesLanguage> list=cmsResourcesLanguageService.findResources(str, CmsResourcesLanguage.video);
            for(CmsResourcesLanguage r:list){
                cmsResourcesLanguageService.deleteById(r.getId());
            }
        }
        return Result.success();
    }
}
