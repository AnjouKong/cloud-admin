
package cn.phenix.cloud.controller.app.cms;

import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsCustomService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.admin.app.vo.cms.CmsCustomVo;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsCustom;
import cn.phenix.model.app.cms.CmsResourcesLanguage;
import cn.phenix.model.app.language.LanguageDic;
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
@RequestMapping("platform/cms/custom")
@Api(tags = "自定义页面管理")
@RequiresPermissions("sys.manager.unit")
public class CmsCustomController {

    @Autowired
    private CmsChannelService cmsChannelService;

    @Autowired
    private ServiceBasicInfoService serviceBasicInfoService;
    @Autowired
    private CmsCustomService cmsCustomService;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;

    @RequestMapping("")
    public String index() {
        return "app/cms/custom/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsCustom> data(DataTableParameter parameter, CmsCustom cmsCustom) {
        PageTable<CmsCustom> pageTable = cmsCustomService.findPage(parameter, cmsCustom);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("service", serviceBasicInfoService.findAll());
        return "app/cms/custom/add";
    }

    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsCustom cmsCustom) {
        cmsCustomService.save(cmsCustom);
        //语言
        for (String language : cmsCustom.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsCustom.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.custom);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "配置")
    public String config(@PathVariable("id") String id, Model model) {
        CmsCustom cmsCustom = cmsCustomService.get(id);
        model.addAttribute("content", cmsCustom.getContent());
        List<CmsResourcesLanguage> languageList = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.custom);
        model.addAttribute("language",languageList);
        return "app/cms/custom/config";
    }

    @PostMapping("/detail")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "自定义页面回显")
    public Result detail(String id) {
        CmsCustom cmsCustom = cmsCustomService.get(id);
        return Result.success().addData(cmsCustom.getContent()!=null?cmsCustom.getContent():null);
    }

    @PostMapping(value = "configDo")
    @ResponseBody
    @ApiOperation(value = "配置保存")
    @RequiresPermissions("sys.manager.unit")
    public Result configDo(@RequestBody CmsCustomVo cmsCustomVo) {
        CmsCustom cmsCustom = cmsCustomService.get(cmsCustomVo.getId());
        cmsCustom.setContent(cmsCustomVo.getContent().toString());
        cmsCustomService.saveOrUpdate(cmsCustom);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsCustomService.get(id));
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("resourceLanguage", cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.custom));
        model.addAttribute("service", serviceBasicInfoService.findAll());
        return "app/cms/custom/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsCustom cmsCustom) {
        cmsCustomService.saveOrUpdate(cmsCustom);
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
        cmsResourcesLanguageService.deleteResources(cmsCustom.getId(), CmsResourcesLanguage.custom);
        //语言
        for (String language : cmsCustom.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsCustom.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.custom);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsCustomService.deleteById(id);
        return Result.success();
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        cmsCustomService.deleteByIds(ids);
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

