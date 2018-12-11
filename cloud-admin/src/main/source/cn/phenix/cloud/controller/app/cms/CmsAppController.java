package cn.phenix.cloud.controller.app.cms;


import cn.phenix.cloud.admin.app.cms.service.CmsAppService;
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
import cn.phenix.model.app.cms.CmsApp;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("platform/cms/app")
@Api(tags = "app管理")
@RequiresPermissions("sys.manager.unit")
public class CmsAppController extends BaseController {

    @Autowired
    private CmsAppService cmsAppService;
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
        return "app/cms/app/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public PageTable<CmsApp> data(DataTableParameter parameter, CmsApp cmsApp) {
        PageTable<CmsApp> pageTable = cmsAppService.findPage(parameter, cmsApp);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String channelId, Model model) {
        model.addAttribute("channel", channelId != null && !"root".equals(channelId) ? cmsChannelService.get(channelId) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/app/add";
    }


    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsApp cmsApp) {
        String param = cmsApp.getParam();
        cmsApp.setParam(param.replaceAll("&quot;", "\""));
        CmsApp app = cmsAppService.save(cmsApp);
        //语言
        for (String language : cmsApp.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsApp.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.App);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success().addData(app);
    }

    @PostMapping(value = "configDo")
    @ResponseBody
    @ApiOperation(value = "配置保存")
    @RequiresPermissions("sys.manager.unit")
    public Result configDo(CmsApp cmsApp, AttMainForm attMainForm) {
        attMainService.updateAttMainForm(attMainForm, cmsApp.getId(), CmsApp.modelName);
        return Result.success();
    }

    @GetMapping("/config/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "配置")
    public String config(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        List<AttMain> list = attMainService.getAttMain(id, CmsApp.modelName);
        List<CmsResourcesLanguage> languageList = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.App);
        for (CmsResourcesLanguage r : languageList) {
            for (AttMain a : list) {
                if (r.getLanguage().equals(a.getFdKey())) {
                    r.setResourcesUrl(a.getFdFileName());
                    r.setAttId(a.getId());
                }

            }
        }
        model.addAttribute("language", languageList);
        model.addAttribute("modelName", CmsApp.modelName);
        model.addAttribute("modelId", id);
        return "app/cms/app/config";
    }


    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    @Deprecated
    public String detail(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsAppService.get(id));
        List<AttMain> list = attMainService.getAttMain(id, CmsApp.modelName, "pic");
        model.addAttribute("pic", list.size() > 0 ? list.get(0).getFdFilePath() : null);
        return "app/cms/app/detail";
    }

    @PostMapping(value = "download")
    @ResponseBody
    @ApiOperation(value = "下载")
    @RequiresPermissions("sys.manager.unit")
    public void download(CmsApp cmsApp, HttpServletResponse response) throws IOException {
        List<AttMain> list = attMainService.getAttMain(cmsApp.getId(), CmsApp.modelName, "apk");
        if (list.size() == 0) throw new IOException("下载的文件不存在");
        URL url = new URL(list.get(0).getFdFilePath());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + list.get(0).getFdFileName() + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        while ((len = inputStream.read(b)) > 0)
            response.getOutputStream().write(b, 0, len);
        inputStream.close();

    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", cmsAppService.get(id));
//        List<AttMain> list = attMainService.getAttMain(id, CmsApp.modelName);
//        model.addAttribute("apk", list.size() > 0 ? list.get(0) : null);
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("resourceLanguage", cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.App));
        model.addAttribute("service", serviceBasicInfoService.findAll());

        return "app/cms/app/edit";
    }


    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsApp cmsApp) {
        String param = cmsApp.getParam();
        cmsApp.setParam(param.replaceAll("&quot;", "\""));
        cmsAppService.saveOrUpdate(cmsApp);
        //attMainService.updateAttMainForm(attMainForm, cmsApp.getId(), CmsApp.modelName);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(cmsApp.getId(), CmsResourcesLanguage.App);
        //删除无关附件
        for (CmsResourcesLanguage r : list) {
            if (!Arrays.asList(cmsApp.getLanguageDic()).contains(r.getLanguage())) {
                List<AttMain> attMainList = attMainService.getAttMain(cmsApp.getId(), CmsApp.modelName, r.getLanguage());
                for (AttMain a : attMainList) {
                    attMainService.deleteAttMainById(a.getId());
                }
            }
        }
        cmsResourcesLanguageService.deleteResources(cmsApp.getId(), CmsResourcesLanguage.App);
        //语言
        for (String language : cmsApp.getLanguageDic()) {
            CmsResourcesLanguage resourcesLanguage = new CmsResourcesLanguage();
            resourcesLanguage.setLanguage(language);
            resourcesLanguage.setResourcesId(cmsApp.getId());
            resourcesLanguage.setResourcesType(CmsResourcesLanguage.App);
            cmsResourcesLanguageService.save(resourcesLanguage);
        }
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsAppService.deleteById(id);
        // attMainService.deleteAttMain(id, CmsApp.modelName);
        List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(id, CmsResourcesLanguage.App);
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
        cmsAppService.deleteByIds(ids);
        for (String str : ids) {
            // attMainService.deleteAttMain(str, CmsApp.modelName);
            List<CmsResourcesLanguage> list = cmsResourcesLanguageService.findResources(str, CmsResourcesLanguage.App);
            for (CmsResourcesLanguage r : list) {
                cmsResourcesLanguageService.deleteById(r.getId());
            }
        }
        return Result.success();
    }
}
