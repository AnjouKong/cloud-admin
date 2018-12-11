package cn.phenix.cloud.controller.app.cms;


import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsHtmlArticleService;
import cn.phenix.cloud.admin.app.cms.service.CmsHtmlService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsHtml;
import cn.phenix.model.app.cms.CmsHtmlArticle;
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
@RequestMapping("platform/cms/html")
@Api(tags = "html管理")
@RequiresPermissions("sys.manager.unit")
public class CmsHtmlController extends BaseController {

    @Autowired
    private CmsHtmlService cmsHtmlService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private CmsChannelService cmsChannelService;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;
    @Autowired
    private CmsHtmlArticleService cmsHtmlArticleService;
    @Autowired
    private ServiceBasicInfoService serviceBasicInfoService;

    @RequestMapping("")
    public String index() {
        return "app/cms/html/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsHtml> data(DataTableParameter parameter, CmsHtml cmsHtml) {
        PageTable<CmsHtml> pageTable = cmsHtmlService.findPage(parameter, cmsHtml);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/html/add";
    }

    @RequestMapping("addArticle/{htmlId}")
    @RequiresPermissions("sys.manager.unit")
    public String addArticle(@PathVariable("htmlId") String htmlId, Model model) {
        model.addAttribute("htmlId", htmlId);
        model.addAttribute("language", cmsResourcesLanguageService.findByResourceId(htmlId));
        model.addAttribute("articleLanguage", cmsHtmlArticleService.findByHtmlIdAndLanguageCode(htmlId, null, null));
        return "app/cms/article/add";
    }

    @GetMapping("/editArticle/{id}")
    @RequiresPermissions("sys.manager.unit")
    public String editArticle(@PathVariable("id") String id, Model model) {
        CmsHtmlArticle article = cmsHtmlArticleService.get(id);
        model.addAttribute("obj", article);
        model.addAttribute("htmlId", article.getHtmlId());
        model.addAttribute("language", cmsResourcesLanguageService.findByResourceId(article.getHtmlId()));
        model.addAttribute("articleLanguage", cmsHtmlArticleService.findByHtmlIdAndLanguageCode(article.getHtmlId(), null, null));
        return "app/cms/article/edit";
    }

    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsHtml cmsHtml, AttMainForm attMainForm) {
        cmsHtmlService.save(cmsHtml);
        attMainService.updateAttMainForm(attMainForm, cmsHtml.getId(), CmsHtml.modelName);
        //语言
        for (String language : cmsHtml.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsHtml.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.html);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @PostMapping(value = "configDo")
    @ResponseBody
    @ApiOperation(value = "配置保存")
    @RequiresPermissions("sys.manager.unit")
    public Result configDo(CmsHtml cmsHtml, AttMainForm attMainForm) {
        attMainService.updateAttMainForm(attMainForm, cmsHtml.getId(), CmsHtml.modelName);
        return Result.success();
    }

    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "配置")
    public String config(@PathVariable("id") String id, Model model) {
        model.addAttribute("htmlId", id);
        List<CmsResourcesLanguage> languageList = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.html);
        model.addAttribute("language", languageList);
        return "app/cms/article/index";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsHtmlService.get(id));
        model.addAttribute("modelName", CmsHtml.modelName);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("resourceLanguage", cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.html));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/html/edit";
    }


    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsHtml cmsHtml, AttMainForm attMainForm) {
        cmsHtmlService.saveOrUpdate(cmsHtml);
        attMainService.updateAttMainForm(attMainForm, cmsHtml.getId(), CmsHtml.modelName);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(cmsHtml.getId(), CmsResourcesLanguage.image);
        //删除无关附件
        for (CmsResourcesLanguage r : list) {
            if (!Arrays.asList(cmsHtml.getLanguageDic()).contains(r.getLanguage())) {
                List<AttMain> attMainList = attMainService.getAttMain(cmsHtml.getId(), CmsHtml.modelName, r.getLanguage());
                for (AttMain a : attMainList) {
                    attMainService.deleteAttMainById(a.getId());
                }
            }
        }
        cmsResourcesLanguageService.deleteResources(cmsHtml.getId(), CmsResourcesLanguage.html);
        //语言
        for (String language : cmsHtml.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsHtml.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.html);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsHtmlService.deleteById(id);
        //attMainService.deleteAttMain(id, CmsImg.modelName);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.html);
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
        cmsHtmlService.deleteByIds(ids);
        for (String str : ids) {
            //attMainService.deleteAttMain(str, CmsImg.modelName);
            List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(str, CmsResourcesLanguage.html);
            for (CmsResourcesLanguage r : list) {
                cmsResourcesLanguageService.deleteById(r.getId());
            }
        }
        return Result.success();
    }
}
