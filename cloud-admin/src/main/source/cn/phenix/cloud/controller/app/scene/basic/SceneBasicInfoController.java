package cn.phenix.cloud.controller.app.scene.basic;

import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourceService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.media.homePage.service.MediaHomePageService;
import cn.phenix.cloud.admin.app.scene.basic.service.*;
import cn.phenix.cloud.admin.app.scene.other.service.SceneLanguageService;
import cn.phenix.cloud.admin.app.vo.scene.SceneSaveVo;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.admin.sys.service.SysCategoryService;
import cn.phenix.cloud.admin.sys.service.SysUserService;
import cn.phenix.cloud.admin.tenant.renter.service.TenantSceneResourceService;
import cn.phenix.cloud.admin.tenant.renter.service.TenantSceneService;
import cn.phenix.cloud.admin.tenant.renter.vo.TenantSceneVo;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.security.Principal;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.security.SecurityUtils;
import cn.phenix.model.app.cms.*;
import cn.phenix.model.app.language.LanguageDic;
import cn.phenix.model.app.media.MediaHomePage;
import cn.phenix.model.app.scene.*;
import cn.phenix.model.sys.SysCategory;
import cn.phenix.model.tenant.renter.TenantScene;
import cn.phenix.model.tenant.renter.TenantSceneResource;
import cn.phenix.model.ui.v1.ColumnUI;
import cn.phenix.model.ui.v1.event.ContainerEvent;
import cn.phenix.model.ui.v1.launcher.Container;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Api(tags = "场景管理")
@RequestMapping("platform/scene/basic")
public class SceneBasicInfoController extends BaseController {

    @Autowired
    private SceneBasicInfoService sceneBasicInfoService;
    @Autowired
    private SceneVersionInfoService sceneVersionInfoService;
    @Autowired
    private SceneResourceService sceneResourceService;
    @Autowired
    private TenantSceneService tenantSceneService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private SceneLanguageService sceneLanguageService;
    @Autowired
    private CmsResourceService cmsResourceService;
    @Autowired
    private SysCategoryService sysCategoryService;
    @Autowired
    private CmsChannelService cmsChannelService;
    @Autowired
    private TenantSceneResourceService tenantSceneResourceService;
    @Autowired
    private SceneBasicInfoReaderService sceneBasicInfoReaderService;
    @Autowired
    private SceneBasicInfoEditorService sceneBasicInfoEditorService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private MediaHomePageService mediaHomePageService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "首页跳转")
    public String index(Model model) {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        Principal principal = (Principal) subject.getPrincipal();
        model.addAttribute("isAdmin", principal.isAdmin());
        model.addAttribute("modelName", SceneBasicInfo.modelName);
        return "app/scene/basic/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "列表查询")
    public PageTable<SceneBasicInfo> data(Integer draw, DataTableParameter parameter, SceneBasicInfo sceneBasicInfo) {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        Principal principal = (Principal) subject.getPrincipal();
        PageTable<SceneBasicInfo> pageTable = sceneBasicInfoService.findPage(parameter, sceneBasicInfo, principal);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @RequestMapping("resourceData")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "资源列表")
    public PageTable<CmsResource> resourceData(Integer draw, DataTableParameter parameter, CmsResource cmsResource) {
        PageTable<CmsResource> pageTable = cmsResourceService.findPage(parameter, cmsResource);
        pageTable.setDraw(draw);
        return pageTable;
    }

    private String getModelName(String type) {
        switch (type) {
            case "img":
                return CmsImg.modelName;
            case "imgCollection":
                return CmsImgCollection.modelName;
            case "pms":
                return CmsPms.modelName;
        }
        return null;
    }

    @RequestMapping("historyData")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "场景历史数据")
    public PageTable<SceneVersionInfo> historyData(Integer draw, DataTableParameter parameter, SceneVersionInfo sceneVersionInfo) {
        PageTable<SceneVersionInfo> pageTable = sceneVersionInfoService.findPage(parameter, sceneVersionInfo);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "新建场景模板")
    public String add(String categoryId, Model model) {
        model.addAttribute("language", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        if (!Strings.isBlank(categoryId)) {
            model.addAttribute("category", sysCategoryService.get(categoryId));
        }
        model.addAttribute("modelName", SceneBasicInfo.modelName);

        return "app/scene/basic/add";
    }

    @GetMapping("history/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转场景历史页面")
    public String history(@PathVariable("id") String id, Model model) {
        model.addAttribute("basicId", id);
        return "app/scene/basic/history";
    }

    @GetMapping("selectReader")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转选择可查看者页面")
    public String selectReader() {
        return "app/scene/basic/selectReader";
    }

    @GetMapping("selectEditor")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转选择可编辑者页面")
    public String selectEditor() {
        return "app/scene/basic/selectEditor";
    }


    @GetMapping("selectBootResource")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转选择开机资源页面")
    public String selectResource() {
        return "app/scene/basic/selectBootResource";
    }

    @GetMapping("selectShutdownResource")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转选择关机资源页面")
    public String selectShutdownResource() {
        return "app/scene/basic/selectShutdownResource";
    }

    @GetMapping("selectThemePackageResource")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转选择主题包资源页面")
    public String selectThemePackageResource() {
        return "app/scene/basic/selectThemePackageResource";
    }

    @GetMapping("selectScreenSaverResource")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转选择屏保资源页面")
    public String selectScreenSaverResource() {
        return "app/scene/basic/selectScreenSaverResource";
    }

    @GetMapping("selectMusicResource")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转选择背景音乐页面")
    public String selectMusicResource() {
        return "app/scene/basic/selectMusicResource";
    }

    @GetMapping("selectMediaHomePage")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转选择vod首页页面")
    public String selectMediaHomePage() {
        return "app/scene/basic/selectMediaHomePage";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "保存")
    public Result addDo(SceneBasicInfo sceneBasicInfo, AttMainForm attMainForm) {

        String sceneId = IdGen.uuid();
        sceneBasicInfo.setId(sceneId);
        sceneBasicInfo.setShowPrice(sceneBasicInfo.getShowPrice() == null ? "0" : "1");
        sceneBasicInfoService.save(sceneBasicInfo);
        attMainService.updateAttMainForm(attMainForm, sceneId, SceneBasicInfo.modelName);//主题包

        //权限信息
        List<SceneBasicInfoReader> readerList = Lists.newArrayList();
        for (String readerId : sceneBasicInfo.getReaderId()) {
            SceneBasicInfoReader reader = new SceneBasicInfoReader();
            reader.setReaderId(readerId);
            reader.setTemplateId(sceneId);
            readerList.add(reader);
        }
        sceneBasicInfoReaderService.saveAll(readerList);

        List<SceneBasicInfoEditor> editorList = Lists.newArrayList();
        for (String editorId : sceneBasicInfo.getEditorId()) {
            SceneBasicInfoEditor editor = new SceneBasicInfoEditor();
            editor.setEditorId(editorId);
            editor.setTemplateId(sceneId);
            editorList.add(editor);
        }
        sceneBasicInfoEditorService.saveAll(editorList);

        //语言
        for (String languageCode : sceneBasicInfo.getLanguageDic()) {
            SceneLanguage language = new SceneLanguage();
            language.setLanguageCode(languageCode);
            language.setSceneId(sceneId);
            sceneLanguageService.save(language);
        }
        //主题包
        if (!Strings.isBlank(sceneBasicInfo.getThemePackageResourceArr())) {
            String themeJson = sceneBasicInfo.getThemePackageResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_theme = JSONArray.fromObject(themeJson);
            for (int i = 0; i < jsonArray_theme.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_theme.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneId);
                sceneResource.setEventType(SceneResource.themePackage);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResourceService.save(sceneResource);
            }
        }
        //背景音乐
        if (!Strings.isBlank(sceneBasicInfo.getMusicResourceArr())) {
            String musicJson = sceneBasicInfo.getMusicResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_music = JSONArray.fromObject(musicJson);
            for (int i = 0; i < jsonArray_music.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_music.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneId);
                sceneResource.setEventType(SceneResource.backgroundMusic);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResource.setBootPlay(map.get("isBootPlay") == null ? false : true);
                sceneResource.setLauncherPlay(map.get("isLauncherPlay") == null ? false : true);
                sceneResourceService.save(sceneResource);
            }
        }
        // 保存屏保资源
        if (!Strings.isBlank(sceneBasicInfo.getScreenSaverResourceArr())) {
            String screenSaverJson = sceneBasicInfo.getScreenSaverResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_boot = JSONArray.fromObject(screenSaverJson);
            for (int i = 0; i < jsonArray_boot.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_boot.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneId);
                sceneResource.setEventType(SceneResource.screenSaverEvent);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setIndex(Strings.isBlank(map.get("index")) ? i : Integer.parseInt(map.get("index")));
                sceneResource.setPlayTime(map.get("playTime") == null ? "0" : map.get("playTime"));
                sceneResource.setSkipTime(map.get("skipTime") == null ? "0" : map.get("skipTime"));
                sceneResource.setSkip(map.get("skip") == null ? false : true);
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResourceService.save(sceneResource);
            }
        }

        // 保存开机资源
        if (!Strings.isBlank(sceneBasicInfo.getBootResourceArr())) {
            String bootJson = sceneBasicInfo.getBootResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_screenSaver = JSONArray.fromObject(bootJson);
            for (int i = 0; i < jsonArray_screenSaver.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_screenSaver.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneId);
                sceneResource.setEventType(SceneResource.bootEvent);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setIndex(Strings.isBlank(map.get("index")) ? i : Integer.parseInt(map.get("index")));
                sceneResource.setPlayTime(map.get("playTime") == null ? "0" : map.get("playTime"));
                sceneResource.setSkipTime(map.get("skipTime") == null ? "0" : map.get("skipTime"));
                sceneResource.setSkip(map.get("skip") == null ? false : true);
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResource.setPhysicalBootResource(map.get("isPhysicalBootResource") == null ? false : true);
                sceneResource.setCarousel(map.get("carousel") == null ? false : true);
                sceneResourceService.save(sceneResource);
            }
        }
        // 保存关机资源
        if (!Strings.isBlank(sceneBasicInfo.getShutdownResourceArr())) {
            String shutdownJson = sceneBasicInfo.getShutdownResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_shutdownJson = JSONArray.fromObject(shutdownJson);
            for (int i = 0; i < jsonArray_shutdownJson.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_shutdownJson.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneId);
                sceneResource.setEventType(SceneResource.shutdownEvent);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setIndex(Strings.isBlank(map.get("index")) ? i : Integer.parseInt(map.get("index")));
                sceneResource.setPlayTime(map.get("playTime") == null ? "0" : map.get("playTime"));
                sceneResource.setSkipTime(map.get("skipTime") == null ? "0" : map.get("skipTime"));
                sceneResource.setSkip(map.get("skip") == null ? false : true);
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResourceService.save(sceneResource);
            }
        }

        return Result.success();
    }

    /**
     * 场景多语言
     *
     * @param id
     * @return
     */
    @PostMapping("/sceneLanguage")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "选择多语言")
    public Result sceneLanguage(String id) {
        List<SceneLanguage> sceneLanguage = sceneLanguageService.findBySceneId(id);
        return Result.success().addData(sceneLanguage);
    }

    /**
     * 场景回显信息
     *
     * @param id
     * @return
     */
    @PostMapping("/detail")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "场景回显")
    public Result detail(String id) {
        SceneVersionInfo info = sceneVersionInfoService.findSceneVersionInfoByBasicId(id, null);
        SceneSaveVo sceneSaveVo = sceneBasicInfoService.findOne(info != null ? info.getUiId() : null);
        return Result.success().addData(sceneSaveVo != null ? sceneSaveVo.getAppUI() : null);
    }

    /**
     * 预览历史信息
     *
     * @param id
     * @return
     */
    @PostMapping("/previewHistory")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "预览场景模板历史信息")
    public Result previewHistory(@Param("id") String id) {
        SceneVersionInfo info = sceneVersionInfoService.get(id);
        SceneSaveVo sceneSaveVo = sceneBasicInfoService.findOne(info != null ? info.getUiId() : null);
        return Result.success().addData(sceneSaveVo != null ? sceneSaveVo.getAppUI() : null);
    }

    /**
     * 预览最新发布信息
     *
     * @param id
     * @return
     */
    @PostMapping("/preview")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "预览场景最新发布信息")
    public Result preview(@Param("id") String id) {
        SceneVersionInfo info = sceneVersionInfoService.findSceneVersionInfoByBasicId(id, SceneVersionInfo.published);
        SceneSaveVo sceneSaveVo = sceneBasicInfoService.findOne(info != null ? info.getUiId() : null);
        return Result.success().addData(sceneSaveVo != null ? sceneSaveVo.getAppUI() : null);
    }

    /**
     * 预览最新发布信息 页面
     *
     * @param id
     * @return
     */
    @GetMapping("/previewHtml/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转预览页面")
    public String previewHtml(@PathVariable("id") String id, Model model) throws Exception {
        if (id == null || "null".equals(id)) return null;
        model.addAttribute("sceneId", id);
        model.addAttribute("type", "latestRelease");
        String url = sceneBasicInfoService.get(id).getStyle() == "" || "delayering".equals(sceneBasicInfoService.get(id).getStyle()) ? "" : "_suspension";
        return "app/scene/basic/preview" + url;
    }

    /**
     * 预览历史信息 页面
     *
     * @param id
     * @return
     */
    @GetMapping("/previewHistoryHtml/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转预览页面")
    public String previewHistoryHtml(@PathVariable("id") String id, Model model) {
        model.addAttribute("sceneId", id);
        model.addAttribute("type", "history");
        String url = sceneBasicInfoService.get(sceneVersionInfoService.get(id).getBasicId()).getStyle() == "" || "delayering".equals(sceneBasicInfoService.get(sceneVersionInfoService.get(id).getBasicId()).getStyle()) ? "" : "_suspension";

        return "app/scene/basic/preview" + url;
    }

    /**
     * 配置场景信息
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/configure/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转配置页面")
    public String configure(@PathVariable("id") String id, Model model) {
        SceneVersionInfo info = sceneVersionInfoService.findSceneVersionInfoByBasicId(id, null);
        model.addAttribute("sceneBasicInfoId", id);
        model.addAttribute("version", info != null ? info.getVersion() : 0);
        SceneBasicInfo basic = sceneBasicInfoService.get(id);
        if (basic == null) return null;
        String url = basic.getStyle() == null || SceneBasicInfo.style_delayering.equals(basic.getStyle()) ? "configure" : "configure_suspension";
        return "app/scene/basic/" + url;
    }

    @PostMapping("configureDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "保存配置")
    public Result configureDo(@RequestBody SceneSaveVo sceneSaveVo) {
        String mongodbId = IdGen.uuid();
        SceneVersionInfo info = new SceneVersionInfo();
        info.setBasicId(sceneSaveVo.getSceneBasicInfoId());
        info.setUiId(mongodbId);
        info.setHashcode(sceneSaveVo.getAppUI().hashCode());
        info.setPublish(SceneVersionInfo.unpublished);
        info.setVersion(Integer.parseInt(sceneSaveVo.getVersion() == null ? "0" : sceneSaveVo.getVersion()) + 1);
        sceneVersionInfoService.save(info);

        sceneSaveVo.setId(mongodbId);
        sceneBasicInfoService.save(sceneSaveVo);

        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        SceneBasicInfo sceneBasicInfo = sceneBasicInfoService.get(id);
        if (!Strings.isBlank(sceneBasicInfo.getMediaHomePageId())) {
            MediaHomePage homePage = mediaHomePageService.get(sceneBasicInfo.getMediaHomePageId());
            sceneBasicInfo.setHomePageName(homePage == null ? "" : homePage.getHomePageName());
        }
        List<SceneLanguage> language = sceneLanguageService.findBySceneId(id);
        model.addAttribute("obj", sceneBasicInfo);
        model.addAttribute("languageDic", languageDicService.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL));
        model.addAttribute("language", language);
        Principal principal = SecurityUtils.getPrincipal();
        model.addAttribute("isCreator", principal.getId().equals(sceneBasicInfo.getCreateBy()) || principal.isAdmin());

        //可编辑者
        List<SceneBasicInfoEditor> editorList = sceneBasicInfoEditorService.findByTemplateId(id);
        String editorName = "";
        String editorId = "";
        for (SceneBasicInfoEditor editor : editorList) {
            editorId += editor.getEditorId() + ",";
            editorName += sysUserService.get(editor.getEditorId()).getName() + ",";
        }
        if (!Strings.isBlank(editorId) && !Strings.isBlank(editorName)) {
            model.addAttribute("editorName", editorName.substring(0, editorName.length() - 1));
            model.addAttribute("editorId", editorId.substring(0, editorId.length() - 1));
        }

        //可查看者
        List<SceneBasicInfoReader> readerList = sceneBasicInfoReaderService.findByTemplateId(id);
        String readerName = "";
        String readerId = "";
        for (SceneBasicInfoReader reader : readerList) {
            readerId += reader.getReaderId() + ",";
            readerName += sysUserService.get(reader.getReaderId()).getName() + ",";
        }
        if (!Strings.isBlank(readerId) && !Strings.isBlank(readerName)) {
            model.addAttribute("readerName", readerName.substring(0, readerName.length() - 1));
            model.addAttribute("readerId", readerId.substring(0, readerId.length() - 1));
        }


        if (!Strings.isBlank(sceneBasicInfo.getCategoryId())) {
            SysCategory category = sysCategoryService.get(sceneBasicInfo.getCategoryId());
            model.addAttribute("category", category != null && SysCategory.DEL_FLAG_NORMAL.equals(category.getDelFlag()) ? category : null);
        }

        List<SceneResource> resourceList = sceneResourceService.findBySceneIdAndEventType(id, null);
        for (SceneResource r : resourceList) {
            CmsResource cms = cmsResourceService.get(r.getResourceId());
            String modelName = getModelName(cms.getResourceType().name());
            if (modelName != null) {
                r.setResource(cms.getId() + "/" + modelName + "/");
            } else {
                r.setResource("");
            }
        }
        model.addAttribute("resourceList", resourceList);
        model.addAttribute("modelName", SceneBasicInfo.modelName);
        return "app/scene/basic/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑保存")
    public Result editDo(SceneBasicInfo sceneBasicInfo, AttMainForm attMainForm) {
        sceneBasicInfo.setShowPrice(sceneBasicInfo.getShowPrice() == null ? "0" : "1");
        sceneBasicInfoService.saveOrUpdate(sceneBasicInfo);
        attMainService.updateAttMainForm(attMainForm, sceneBasicInfo.getId(), SceneBasicInfo.modelName);

        //权限
        sceneBasicInfoReaderService.deleteByTemplateId(sceneBasicInfo.getId());
        sceneBasicInfoEditorService.deleteByTemplateId(sceneBasicInfo.getId());

        List<SceneBasicInfoReader> readerList = Lists.newArrayList();
        for (String readerId : sceneBasicInfo.getReaderId()) {
            SceneBasicInfoReader reader = new SceneBasicInfoReader();
            reader.setReaderId(readerId);
            reader.setTemplateId(sceneBasicInfo.getId());
            readerList.add(reader);
        }
        sceneBasicInfoReaderService.saveAll(readerList);

        List<SceneBasicInfoEditor> editorList = Lists.newArrayList();
        for (String editorId : sceneBasicInfo.getEditorId()) {
            SceneBasicInfoEditor editor = new SceneBasicInfoEditor();
            editor.setEditorId(editorId);
            editor.setTemplateId(sceneBasicInfo.getId());
            editorList.add(editor);
        }
        sceneBasicInfoEditorService.saveAll(editorList);

        //语言
        sceneLanguageService.deleteBySceneId(sceneBasicInfo.getId());
        for (String languageCode : sceneBasicInfo.getLanguageDic()) {
            SceneLanguage language = new SceneLanguage();
            language.setLanguageCode(languageCode);
            language.setSceneId(sceneBasicInfo.getId());
            sceneLanguageService.save(language);
        }
        sceneResourceService.deleteBySceneId(sceneBasicInfo.getId());
        //主题包
        if (!Strings.isBlank(sceneBasicInfo.getThemePackageResourceArr())) {
            String themeJson = sceneBasicInfo.getThemePackageResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_theme = JSONArray.fromObject(themeJson);
            for (int i = 0; i < jsonArray_theme.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_theme.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneBasicInfo.getId());
                sceneResource.setEventType(SceneResource.themePackage);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResourceService.save(sceneResource);
            }
        }
        //背景音乐
        if (!Strings.isBlank(sceneBasicInfo.getMusicResourceArr())) {
            String musicJson = sceneBasicInfo.getMusicResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_music = JSONArray.fromObject(musicJson);
            for (int i = 0; i < jsonArray_music.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_music.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneBasicInfo.getId());
                sceneResource.setEventType(SceneResource.backgroundMusic);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResource.setBootPlay(map.get("isBootPlay") == null ? false : true);
                sceneResource.setLauncherPlay(map.get("isLauncherPlay") == null ? false : true);
                sceneResourceService.save(sceneResource);
            }
        }

        // 保存屏保资源
        if (!Strings.isBlank(sceneBasicInfo.getScreenSaverResourceArr())) {
            String screenSaverJson = sceneBasicInfo.getScreenSaverResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_boot = JSONArray.fromObject(screenSaverJson);
            for (int i = 0; i < jsonArray_boot.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_boot.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneBasicInfo.getId());
                sceneResource.setEventType(SceneResource.screenSaverEvent);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setIndex(Strings.isBlank(map.get("index")) ? i : Integer.parseInt(map.get("index")));
                sceneResource.setPlayTime(map.get("playTime") == null ? "0" : map.get("playTime"));
                sceneResource.setSkipTime(map.get("skipTime") == null ? "0" : map.get("skipTime"));
                sceneResource.setSkip(map.get("skip") == null ? false : true);
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResourceService.save(sceneResource);
            }
        }

        // 保存开机资源
        if (!Strings.isBlank(sceneBasicInfo.getBootResourceArr())) {
            String bootJson = sceneBasicInfo.getBootResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_screenSaver = JSONArray.fromObject(bootJson);
            for (int i = 0; i < jsonArray_screenSaver.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_screenSaver.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneBasicInfo.getId());
                sceneResource.setEventType(SceneResource.bootEvent);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setIndex(Strings.isBlank(map.get("index")) ? i : Integer.parseInt(map.get("index")));
                sceneResource.setPlayTime(map.get("playTime") == null ? "0" : map.get("playTime"));
                sceneResource.setSkipTime(map.get("skipTime") == null ? "0" : map.get("skipTime"));
                sceneResource.setSkip(map.get("skip") == null ? false : true);
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResource.setPhysicalBootResource(map.get("isPhysicalBootResource") == null ? false : true);
                sceneResource.setCarousel(map.get("carousel") == null ? false : true);
                sceneResourceService.save(sceneResource);
            }
        }
        // 保存关机资源
        if (!Strings.isBlank(sceneBasicInfo.getShutdownResourceArr())) {
            String shutdownJson = sceneBasicInfo.getShutdownResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_shutdownJson = JSONArray.fromObject(shutdownJson);
            for (int i = 0; i < jsonArray_shutdownJson.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_shutdownJson.get(i);
                SceneResource sceneResource = new SceneResource();
                sceneResource.setSceneId(sceneBasicInfo.getId());
                sceneResource.setEventType(SceneResource.shutdownEvent);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setIndex(Strings.isBlank(map.get("index")) ? i : Integer.parseInt(map.get("index")));
                sceneResource.setPlayTime(map.get("playTime") == null ? "0" : map.get("playTime"));
                sceneResource.setSkipTime(map.get("skipTime") == null ? "0" : map.get("skipTime"));
                sceneResource.setSkip(map.get("skip") == null ? false : true);
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResourceService.save(sceneResource);
            }
        }

        //使用此模板的商户做信息替换
        List<TenantScene> tenantSceneList = tenantSceneService.findBySceneId(sceneBasicInfo.getId());
        for (TenantScene t : tenantSceneList) {
            //基本信息更新
            t.setResolution(sceneBasicInfo.getResolution());
            t.setStyle(sceneBasicInfo.getStyle());
            t.setProportion(sceneBasicInfo.getProportion());
            t.setOpenTheme(sceneBasicInfo.isOpenTheme());
            t.setMediaHomePageId(sceneBasicInfo.getMediaHomePageId());
            t.setShowPrice(sceneBasicInfo.getShowPrice());
            tenantSceneService.saveOrUpdate(t);

            //沿用模板的资源统一删除
            tenantSceneResourceService.deleteByTenantSceneIdAndSucceed(t.getId());
            //资源
            List<SceneResource> sceneResourceList = sceneResourceService.findBySceneIdAndEventType(sceneBasicInfo.getId(), null);
            List<TenantSceneResource> tenantSceneResourceList = Lists.newArrayList();
            for (SceneResource resource : sceneResourceList) {
                TenantSceneResource tenantSceneResource = new TenantSceneResource();
                BeanUtils.copyProperties(resource, tenantSceneResource);
                tenantSceneResource.setId(IdGen.uuid());
                tenantSceneResource.setTenantSceneId(t.getId());
                tenantSceneResource.setSucceed(true);
                tenantSceneResourceList.add(tenantSceneResource);
            }
            tenantSceneResourceService.saveAdvList(tenantSceneResourceList);
        }
        return Result.success();
    }


    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "删除")
    public Result delete(@PathVariable("id") String id) {
        sceneBasicInfoService.deleteById(id);
        sceneResourceService.updateBySceneId(id);
        sceneBasicInfoEditorService.updateFlagByTemplateId(id);
        sceneBasicInfoReaderService.updateFlagByTemplateId(id);
        return Result.success();
    }

    @PostMapping("deletes")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "批量删除")
    public Result deletes(@Param("ids") String[] ids) {
        sceneBasicInfoService.deleteByIds(ids);
        for (String id : ids) {
            sceneResourceService.updateBySceneId(id);
            sceneBasicInfoEditorService.updateFlagByTemplateId(id);
            sceneBasicInfoReaderService.updateFlagByTemplateId(id);
        }
        return Result.success();
    }


    @PostMapping("copy")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "复制")
    public Result copy(@Param("id") String id, @Param("sceneName") String sceneName) {
        SceneBasicInfo oldScene = sceneBasicInfoService.get(id);
        if (oldScene == null) return Result.error("需要复制的模板不存在");

        //基本信息复制
        String sceneId = IdGen.uuid();
        SceneBasicInfo newScene = new SceneBasicInfo();
        BeanUtils.copyProperties(oldScene, newScene);
        newScene.setSceneName(sceneName);
        newScene.setId(sceneId);
        sceneBasicInfoService.save(newScene);

        //语言
        List<SceneLanguage> sceneLanguage = sceneLanguageService.findBySceneId(id);
        for (SceneLanguage language : sceneLanguage) {
            SceneLanguage newLanguage = new SceneLanguage();
            newLanguage.setLanguageCode(language.getLanguageCode());
            newLanguage.setSceneId(sceneId);
            sceneLanguageService.save(newLanguage);
        }

        SceneVersionInfo info = sceneVersionInfoService.findSceneVersionInfoByBasicId(id, SceneVersionInfo.published);
        if (info != null) {
            //数据
            SceneSaveVo sceneSaveVo = sceneBasicInfoService.findOne(info.getUiId());

            String mongodbId = IdGen.uuid();
            SceneVersionInfo newInfo = new SceneVersionInfo();
            newInfo.setBasicId(sceneId);
            newInfo.setUiId(mongodbId);
            newInfo.setHashcode(sceneSaveVo.getAppUI().hashCode());
            newInfo.setPublish(SceneVersionInfo.published);
            newInfo.setVersion(0);
            sceneVersionInfoService.save(newInfo);

            SceneSaveVo vo = new SceneSaveVo();
            BeanUtils.copyProperties(sceneSaveVo, vo);
            vo.setId(mongodbId);
            sceneBasicInfoService.save(vo);

            //资源
            List<SceneResource> sceneResourceList = sceneResourceService.findBySceneIdAndEventType(id, null);
            List<SceneResource> newSceneResourceList = Lists.newArrayList();
            for (SceneResource resource : sceneResourceList) {
                SceneResource sceneResource = new SceneResource();
                BeanUtils.copyProperties(resource, sceneResource);
                sceneResource.setId(IdGen.uuid());
                sceneResource.setSceneId(sceneId);
                newSceneResourceList.add(sceneResource);
            }
            sceneResourceService.saveAll(newSceneResourceList);
        }
        //  编辑者，查看者
        sceneBasicInfoReaderService.findByTemplateId(id);
        List<SceneBasicInfoReader> readerList = sceneBasicInfoReaderService.findByTemplateId(id);
        List<SceneBasicInfoReader> newReaderList = Lists.newArrayList();
        for (SceneBasicInfoReader reader : readerList) {
            SceneBasicInfoReader newReader = new SceneBasicInfoReader();
            newReader.setReaderId(reader.getReaderId());
            newReader.setTemplateId(sceneId);
            newReaderList.add(newReader);
        }
        sceneBasicInfoReaderService.saveAll(newReaderList);

        List<SceneBasicInfoEditor> editorList = sceneBasicInfoEditorService.findByTemplateId(id);
        List<SceneBasicInfoEditor> newEditorList = Lists.newArrayList();
        for (SceneBasicInfoEditor editor : editorList) {
            SceneBasicInfoEditor newEditor = new SceneBasicInfoEditor();
            newEditor.setEditorId(editor.getEditorId());
            newEditor.setTemplateId(sceneId);
            newEditorList.add(newEditor);
        }
        sceneBasicInfoEditorService.saveAll(newEditorList);

        return Result.success();
    }

    @GetMapping("/tree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "获取资源树")
    public List<Map<String, Object>> tree(String type) {
        List<Map<String, Object>> tree = new ArrayList<>();
        if (Strings.isBlank(type)) {
            for (ResourceEnum resource : ResourceEnum.values()) {
                Map<String, Object> obj = new HashMap<>();
                obj.put("id", resource);
                obj.put("text", resource.getText());
                obj.put("children", true);
                tree.add(obj);
            }
        } else {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", type);
            obj.put("text", ResourceEnum.valueOf(type).getText());
            obj.put("children", true);
            tree.add(obj);
        }

        return tree;
    }

    @GetMapping("/channelTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "获取栏目树")
    public List<Map<String, Object>> channelTree(String pid) {
        String type = "";
        for (ResourceEnum resource : ResourceEnum.values()) {
            if (resource.toString().equals(pid)) {
                type = resource.toString();
                pid = "";
                break;
            }
        }

        List<CmsChannel> list = cmsChannelService.findByParentIdAndType(pid, type, "0");
        List<Map<String, Object>> tree = new ArrayList<>();
        for (CmsChannel channel : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", channel.getId());
            obj.put("text", channel.getName());
            obj.put("channelType", channel.getType());
            obj.put("children", channel.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    /**
     * 重加载
     *
     * @param id
     * @return
     */
    @PostMapping("reload/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "重加载")
    public Result reload(@PathVariable("id") String id) {
        SceneVersionInfo info = sceneVersionInfoService.findSceneVersionInfoByBasicId(id, SceneVersionInfo.published);
        if (info != null) {
            SceneSaveVo sceneSaveVo = sceneBasicInfoService.findOne(info.getUiId());
            //使用此模板的商户做替换
            List<TenantScene> tenantSceneList = tenantSceneService.findBySceneId(id);
            for (TenantScene t : tenantSceneList) {
                TenantSceneVo vo = tenantSceneService.findOne(t.getUiId());
                vo.setHashcode(info.getHashcode());//hash值
                //修改
                vo.setAppUI(sceneSaveVo.getAppUI());
                tenantSceneService.editMongodb(vo);
            }
            return Result.success();
        }
        return Result.error("不存在发布的信息");
    }

    /**
     * 恢复历史版本
     *
     * @return
     */
    @PostMapping("recovery")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "恢复历史版本")
    public Result recovery(@Param("basicId") String basicId, @Param("versionId") String versionId) {
        SceneVersionInfo info = sceneVersionInfoService.get(versionId);
        SceneSaveVo sceneSaveVo = sceneBasicInfoService.findOne(info.getUiId());
        //mongo数据
        SceneSaveVo newVo = new SceneSaveVo();
        BeanUtils.copyProperties(sceneSaveVo, newVo);
        String uiId = IdGen.uuid();
        newVo.setId(uiId);
        sceneBasicInfoService.save(newVo);

        //版本数据
        SceneVersionInfo newestInfo = sceneVersionInfoService.findSceneVersionInfoByBasicId(basicId, null);
        SceneVersionInfo newInfo = new SceneVersionInfo();
        newInfo.setBasicId(basicId);
        newInfo.setVersion(newestInfo.getVersion() + 1);
        newInfo.setHashcode(info.getHashcode());
        newInfo.setUiId(uiId);
        newInfo.setPublish(SceneVersionInfo.published);
        sceneVersionInfoService.save(newInfo);

        //使用此模板的商户做替换
        List<TenantScene> tenantSceneList = tenantSceneService.findBySceneId(basicId);
        for (TenantScene t : tenantSceneList) {
            //只更新场景信息
            TenantSceneVo vo = tenantSceneService.findOne(t.getUiId());
            vo.setHashcode(newInfo.getHashcode());//hash值
            //修改
            vo.setAppUI(newVo.getAppUI());
            tenantSceneService.editMongodb(vo);
        }

        return Result.success();
    }

    /**
     * 发布
     *
     * @param id
     * @return
     */
    @PostMapping("enable/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "发布")
    public Result enable(@PathVariable("id") String id) {
        SceneVersionInfo info = sceneVersionInfoService.findSceneVersionInfoByBasicId(id, null);
        if (info != null && SceneVersionInfo.unpublished.equals(info.getPublish())) {
            info.setPublish(SceneVersionInfo.published);
            sceneVersionInfoService.saveOrUpdate(info);
//            SceneSaveVo sceneSaveVo = sceneBasicInfoService.findOne(info.getUiId());
//
//            //使用此模板的商户做同位置替换
//            List<TenantScene> tenantSceneList = tenantSceneService.findBySceneId(id);
//            for (TenantScene t : tenantSceneList) {
//                TenantSceneVo vo = tenantSceneService.findOne(t.getUiId());
//                vo.setHashcode(info.getHashcode());//hash值
//                AppUI ui = replaceAppUI(sceneSaveVo.getAppUI(), vo.getAppUI());
//                //修改
//                vo.setAppUI(ui);
//                tenantSceneService.edit(vo);
//            }

            return Result.success();
        }
        return Result.error("不存在未发布的信息");
    }

    /**
     * 替换容器信息
     *
     * @param sourceContainer 新容器
     * @param targetContainer 目标容器
     */
    private <T extends Container> T replaceAppUI(T sourceContainer, T targetContainer) {
        if (!Strings.isBlank(sourceContainer.getIsAuthorize()) && "0".equals(sourceContainer.getIsAuthorize())) {  //不允许二级编辑
            targetContainer = sourceContainer;
        } else {
            targetContainer.setIsAuthorize("1");
            recursionSubContainer(sourceContainer, targetContainer);
        }
        return targetContainer;
    }

    /**
     * 递归子容器
     *
     * @param sourceContainer 新容器
     * @param targetContainer 目标容器
     */
    private void recursionSubContainer(Container sourceContainer, Container targetContainer) {
        for (Container container : sourceContainer.getSubContainer()) {

            List<String> targetList = Lists.newArrayList();
            List<String> replaceList = Lists.newArrayList();
            recursionFindContainerById(container, targetContainer, targetList, replaceList);

            // 未找到直接新添，找到后继续递归
            if (targetList.size() > 0) {
                //判断是否为栏目、栏目动作是否为容器事件
                if ("1".equals(container.getIsAuthorize()) &&
                        ColumnUI.uiClassID.equals(container.getUiClass()) &&
                        container.getSceneEvents().get(0).getEvent().equals(ContainerEvent.eventID)) {

                    ContainerEvent event = (ContainerEvent) container.getSceneEvents().get(0);
                    recursionColumnUI((List<Container>) event.getRule().getSubContainer(), targetContainer, container.getId());
                }
                if (replaceList.size() == 0) {   //代表未被替换
                    recursionSubContainer(container, targetContainer);

                }
            } else {
                //未找到此容器，添加
                addNewContainer(sourceContainer.getId(), container, targetContainer);
            }
        }

    }


    /**
     * 按照Id将新容器添加到目标容器的指定位置
     *
     * @param parentId        id
     * @param newContainer    新容器
     * @param targetContainer 目标容器
     */
    private void addNewContainer(String parentId, Container newContainer, Container targetContainer) {
        for (Container c : targetContainer.getSubContainer()) {
            if (parentId.equals(c.getId())) {
                List<Container> list = (List<Container>) c.getSubContainer();
                list.add(newContainer);
                c.setSubContainer(list);
                break;
            } else {
                addNewContainer(parentId, newContainer, c);
            }

        }
    }


    /**
     * 替换商家推荐位
     *
     * @param subContainer    推荐位集合
     * @param targetContainer 目标容器
     * @param columnUIId      栏目Id
     */
    private void recursionColumnUI(List<Container> subContainer, Container targetContainer, String columnUIId) {

        for (Container c : targetContainer.getSubContainer()) {
            //判断是否为栏目、栏目动作是否为容器事件
            if (ColumnUI.uiClassID.equals(c.getUiClass()) &&
                    c.getSceneEvents().get(0).getEvent().equals(ContainerEvent.eventID) &&
                    columnUIId.equals(c.getId())) {  //相同栏目

                ContainerEvent event = (ContainerEvent) c.getSceneEvents().get(0);

                for (Container container : subContainer) {
                    boolean flag = true; //标识，商户端是否存在此推荐位
                    for (Container eventContainer : event.getRule().getSubContainer()) {
                        if (eventContainer.getId().equals(container.getId())) {
                            flag = false;
                            if (!Strings.isBlank(container.getIsAuthorize()) && "0".equals(container.getIsAuthorize())) {
                                List<Container> list = (List<Container>) event.getRule().getSubContainer();
                                list.add(container);
                                list.remove(eventContainer);
                                event.getRule().setSubContainer(list);
                            } else if (Strings.isBlank(eventContainer.getIsAuthorize()) || "0".equals(eventContainer.getIsAuthorize())) {  //处理--商户端容器为0，后场景模板容器改为1
                                eventContainer.setIsAuthorize("1");
                            }
                            //目标容器找到
                            break;
                        }

                    }
                    // 此推荐位在相同栏目未存在，新添
                    if (flag) {
                        List<Container> list = (List<Container>) event.getRule().getSubContainer();
                        list.add(container);
                        event.getRule().setSubContainer(list);
                    }

                }

            } else {
                recursionColumnUI(subContainer, c, columnUIId);
            }

        }

    }

    /**
     * 依照id获取容器信息并替换
     *
     * @param sourceContainer
     * @param targetContainer
     * @return
     */
    private void recursionFindContainerById(Container sourceContainer, Container targetContainer, List<String> targetList, List<String> replaceList) {
        for (Container c : targetContainer.getSubContainer()) {

            if (sourceContainer.getId().equals(c.getId())) {
                if (!Strings.isBlank(sourceContainer.getIsAuthorize()) && "0".equals(sourceContainer.getIsAuthorize())) {
                    List<Container> list = (List<Container>) targetContainer.getSubContainer();
                    list.add(sourceContainer);
                    list.remove(c);
                    list.remove(c);
                    targetContainer.setSubContainer(list);
                    replaceList.add("success");
                } else if (Strings.isBlank(c.getIsAuthorize()) || "0".equals(c.getIsAuthorize())) {  //处理--商户端容器为0，后场景模板容器改为1
                    c.setIsAuthorize("1");
                }
                //目标容器找到
                targetList.add("success");
                break;
            } else if (c.getSubContainer().size() > 0) {  //若无子集则结束此层递归
                recursionFindContainerById(sourceContainer, c, targetList, replaceList);
            }

        }

    }

}
