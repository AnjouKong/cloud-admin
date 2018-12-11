package cn.phenix.cloud.controller.app.cms;


import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.cms.service.CmsWebsiteService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.service.service.ServiceBasicInfoService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsWebsite;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("platform/cms/website")
@Api(tags = "网站管理")
@RequiresPermissions("sys.manager.unit")
public class CmsWebsiteController extends BaseController {

    @Autowired
    private CmsWebsiteService cmsWebsiteService;
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
        return "app/cms/website/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsWebsite> data(DataTableParameter parameter, CmsWebsite cmsWebsite) {
        PageTable<CmsWebsite> pageTable = cmsWebsiteService.findPage(parameter, cmsWebsite);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("service", serviceBasicInfoService.findAll());
        return "app/cms/website/add";
    }

    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsWebsite cmsWebsite ) {
        CmsWebsite app = cmsWebsiteService.save(cmsWebsite);
        return Result.success().addData(app);
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    @Deprecated
    public String detail(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsWebsiteService.get(id));
        return "app/cms/website/detail";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsWebsiteService.get(id));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/website/edit";
    }


    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsWebsite cmsWebsite ) {
        cmsWebsiteService.saveOrUpdate(cmsWebsite);
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsWebsiteService.deleteById(id);
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        cmsWebsiteService.deleteByIds(ids);
        return Result.success();
    }
}
