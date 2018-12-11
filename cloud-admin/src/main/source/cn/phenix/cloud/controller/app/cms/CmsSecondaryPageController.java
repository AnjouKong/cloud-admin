
package cn.phenix.cloud.controller.app.cms;

import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.cms.service.CmsSecondaryPageService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.admin.app.vo.cms.CmsSecondaryPageVo;
import cn.phenix.cloud.admin.app.vo.cms.FileVo;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsResourcesLanguage;
import cn.phenix.model.app.cms.CmsSecondaryPage;
import cn.phenix.model.app.language.LanguageDic;
import cn.phenix.model.mechanism.AttMain;
import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.xdevapi.JsonArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by dwp on 2018/9/27.
 */

@Controller
@RequestMapping("platform/cms/secondary")
@Api(tags = "自定义页面管理")
@RequiresPermissions("sys.manager.unit")
public class CmsSecondaryPageController {

    @Autowired
    private CmsChannelService cmsChannelService;

    @Autowired
    private ServiceBasicInfoService serviceBasicInfoService;
    @Autowired
    private CmsSecondaryPageService cmsSecondaryPageService;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;
    @Autowired
    private AttMainService attMainService;

    @RequestMapping("")
    public String index() {
        return "app/cms/secondary/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsSecondaryPage> data(DataTableParameter parameter, CmsSecondaryPage cmsSecondaryPage) {
        PageTable<CmsSecondaryPage> pageTable = cmsSecondaryPageService.findPage(parameter, cmsSecondaryPage);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("service", serviceBasicInfoService.findAll());
        return "app/cms/secondary/add";
    }

    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsSecondaryPage cmsSecondaryPage) {
        cmsSecondaryPageService.save(cmsSecondaryPage);
        //语言
        for (String language : cmsSecondaryPage.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsSecondaryPage.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.secondary);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "配置")
    public String config(@PathVariable("id") String id, Model model) {
        CmsSecondaryPage cmsSecondaryPage = cmsSecondaryPageService.get(id);
        model.addAttribute("id", id);
        model.addAttribute("style", cmsSecondaryPage.getStyle());
        List<CmsResourcesLanguage> languageList = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.secondary);
        model.addAttribute("language", languageList);
        model.addAttribute("modelName", CmsSecondaryPage.modelName);
        return "app/cms/secondary/config";
    }

    @PostMapping("/detail")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "自定义页面回显")
    public Result detail(String id) {
        CmsSecondaryPage cmsSecondaryPage = cmsSecondaryPageService.get(id);
        return Result.success().addData(cmsSecondaryPage.getContent() != null ? cmsSecondaryPage.getContent() : null);
    }

    @PostMapping(value = "configDo")
    @ResponseBody
    @ApiOperation(value = "配置保存")
    @RequiresPermissions("sys.manager.unit")
    public Result configDo(@RequestBody CmsSecondaryPageVo cmsSecondaryPageVo, AttMainForm attMainForm) {
        CmsSecondaryPage cmsSecondaryPage = cmsSecondaryPageService.get(cmsSecondaryPageVo.getId());
        cmsSecondaryPage.setContent(cmsSecondaryPageVo.getContent().toString());
        cmsSecondaryPageService.saveOrUpdate(cmsSecondaryPage);
        attMainService.updateAttMainForm(attMainForm, cmsSecondaryPageVo.getId(), CmsSecondaryPage.modelName);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsSecondaryPageService.get(id));
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("resourceLanguage", cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.secondary));
        model.addAttribute("service", serviceBasicInfoService.findAll());
        return "app/cms/secondary/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsSecondaryPage cmsSecondaryPage) {
        cmsSecondaryPageService.saveOrUpdate(cmsSecondaryPage);
        /*List<CmsCustom> list = cmsResourcesLanguageService.findResources(cmsCustom.getId(), CmsResourcesLanguage.custom);
        //删除无关附件
        for (CmsResourcesLanguage r : list) {
            if (!Arrays.asList(cmsCustom.getLanguageDic()).contains(r.getLanguage())) {
                List<AttMain> attMainList = attMainService.getAttMain(cmsCustom.getId(), CmsCustom.modelName, r.getLanguage());
                for (AttMain a : attMainList) {
                    attMainService.deleteAttMainById(a.getId());
                }
            }
        }*/
        cmsResourcesLanguageService.deleteResources(cmsSecondaryPage.getId(), CmsResourcesLanguage.secondary);
        //语言
        for (String language : cmsSecondaryPage.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsSecondaryPage.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.secondary);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsSecondaryPageService.deleteById(id);
        return Result.success();
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        cmsSecondaryPageService.deleteByIds(ids);
        for (String str : ids) {
            //attMainService.deleteAttMain(str, CmsImg.modelName);
            List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(str, CmsResourcesLanguage.custom);
            for (CmsResourcesLanguage r : list) {
                cmsResourcesLanguageService.deleteById(r.getId());
            }
        }
        return Result.success();
    }

}

