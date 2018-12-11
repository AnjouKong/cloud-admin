package cn.phenix.cloud.controller.tenant.renter;

import cn.phenix.cloud.admin.app.cms.service.CmsResourceService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.scene.basic.service.SceneBasicInfoService;
import cn.phenix.cloud.admin.app.scene.basic.service.SceneResourceService;
import cn.phenix.cloud.admin.app.scene.basic.service.SceneVersionInfoService;
import cn.phenix.cloud.admin.app.scene.other.service.SceneLanguageService;
import cn.phenix.cloud.admin.app.vo.scene.SceneSaveVo;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceUserGroupService;
import cn.phenix.cloud.admin.tenant.renter.service.TenantLanguageService;
import cn.phenix.cloud.admin.tenant.renter.service.TenantSceneResourceService;
import cn.phenix.cloud.admin.tenant.renter.service.TenantSceneService;
import cn.phenix.cloud.admin.tenant.renter.vo.TenantSceneVo;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.scene.SceneBasicInfo;
import cn.phenix.model.app.scene.SceneResource;
import cn.phenix.model.app.scene.SceneVersionInfo;
import cn.phenix.model.tenant.device.DeviceUserGroup;
import cn.phenix.model.tenant.renter.TenantLanguage;
import cn.phenix.model.tenant.renter.TenantScene;
import cn.phenix.model.tenant.renter.TenantSceneResource;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 租户场景管理
 */
@Controller
@RequestMapping("platform/tenant/scene")
@Api(tags = "租户场景管理")
public class TenantSceneController extends BaseController {

    @Autowired
    private TenantSceneService tenantSceneService;
    @Autowired
    private SceneBasicInfoService sceneBasicInfoService;
    @Autowired
    private SceneVersionInfoService sceneVersionInfoService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private SceneResourceService sceneResourceService;
    @Autowired
    private TenantSceneResourceService tenantSceneResourceService;
    @Autowired
    private CmsResourceService cmsResourceService;
    @Autowired
    private TenantLanguageService tenantLanguageService;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private SceneLanguageService sceneLanguageService;
    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;
    @Autowired
    private DeviceUserGroupService deviceUserGroupService;

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation("首页数据加载")
    public PageTable<TenantScene> data(TenantScene tenantScene, Integer draw, DataTableParameter parameter) {
        PageTable<TenantScene> pageTable = tenantSceneService.findPage(parameter, tenantScene);
        pageTable.setDraw(draw);
        for (TenantScene scene : pageTable.getData()) {
            scene.setTemplateName(sceneBasicInfoService.get(scene.getSceneId()) == null ? null : sceneBasicInfoService.get(scene.getSceneId()).getSceneName());
            List<DeviceUserGroup> list = deviceUserGroupService.findByTenantSceneId(scene.getId());
            if (list != null && list.size() > 0) {
                String groupName = "";
                for (DeviceUserGroup group : list) {
                    groupName += group.getGroupName() + ",";
                }
                scene.setGroupName(groupName.substring(0, groupName.length() - 1));
            }
        }
        return pageTable;
    }

    @RequestMapping("selectScene")
    @ApiOperation("选择场景")
    @RequiresPermissions("sys.manager.unit")
    public String selectScene(Model model) {
        model.addAttribute("modelName", SceneBasicInfo.modelName);
        return "tenant/tenant/scene/selectScene";
    }

    @GetMapping("selectBootResource")
    @ApiOperation("跳转选择开机资源页面")
    @RequiresPermissions("sys.manager.unit")
    public String selectResource() {
        return "tenant/tenant/scene/selectBootResource";
    }

    @GetMapping("selectShutdownResource")
    @ApiOperation("跳转选择关机资源页面")
    @RequiresPermissions("sys.manager.unit")
    public String selectShutdownResource() {
        return "tenant/tenant/scene/selectShutdownResource";
    }

    @GetMapping("selectScreenSaverResource")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("跳转选择屏保资源页面")
    public String selectScreenSaverResource() {
        return "tenant/tenant/scene/selectScreenSaverResource";
    }

    @GetMapping("selectMusicResource")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("跳转选择背景音乐页面")
    public String selectMusicResource() {
        return "tenant/tenant/scene/selectMusicResource";
    }


    @RequestMapping("add/{tenantId}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "增加页面跳转")
    public String add(@PathVariable("tenantId") String tenantId, Model model) {
        model.addAttribute("tenantId", tenantId);
        return "tenant/tenant/scene/add";
    }


    @PostMapping(value = "/addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(TenantScene tenantScene) {

        if (Strings.isBlank(tenantScene.getSceneId())) return Result.error();

        SceneVersionInfo info = sceneVersionInfoService.findSceneVersionInfoByBasicId(tenantScene.getSceneId(), SceneVersionInfo.published);

        if (info == null) return Result.error("此模板不存在已发布的场景信息");

        SceneSaveVo sceneSaveVo = sceneBasicInfoService.findOne(info != null ? info.getUiId() : null);

        String mongodbId = "";
        if (sceneSaveVo != null) {
            TenantSceneVo vo = new TenantSceneVo();
            vo.setAppUI(sceneSaveVo.getAppUI());
            mongodbId = IdGen.uuid();
            vo.setId(mongodbId);
            vo.setHashcode(info.getHashcode());
            tenantSceneService.saveMongodb(vo); //mongoDB保存
        }
        String modelId = IdGen.uuid();

        SceneBasicInfo sceneBasicInfo = sceneBasicInfoService.get(tenantScene.getSceneId());
        //基本信息
        tenantScene.setDormancyMinute(sceneBasicInfo.getDormancyMinute());
        tenantScene.setScreenSaverMinute(sceneBasicInfo.getScreenSaverMinute());
        tenantScene.setResolution(sceneBasicInfo.getResolution());
        tenantScene.setStyle(sceneBasicInfo.getStyle());
        tenantScene.setProportion(sceneBasicInfo.getProportion());
        tenantScene.setOpenTheme(sceneBasicInfo.isOpenTheme());
        tenantScene.setMediaHomePageId(sceneBasicInfo.getMediaHomePageId());
        tenantScene.setShowPrice(sceneBasicInfo.getShowPrice());

        // 资源
        List<SceneResource> sceneResourceList = sceneResourceService.findBySceneIdAndEventType(tenantScene.getSceneId(), null);
        List<TenantSceneResource> tenantSceneResourceList = Lists.newArrayList();
        for (SceneResource resource : sceneResourceList) {
            TenantSceneResource tenantSceneResource = new TenantSceneResource();
            BeanUtils.copyProperties(resource, tenantSceneResource);
            tenantSceneResource.setId(IdGen.uuid());
            tenantSceneResource.setTenantSceneId(modelId);
            tenantSceneResource.setSucceed(true);
            tenantSceneResourceList.add(tenantSceneResource);
        }
        tenantSceneResourceService.saveAdvList(tenantSceneResourceList);


        tenantScene.setId(modelId);
        tenantScene.setUiId(mongodbId);
        tenantSceneService.save(tenantScene);
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        tenantSceneService.deleteById(id);
        tenantSceneResourceService.updateByTenantSceneId(id);
//      tenantSceneService.delete(tenantSceneService.get(id).getUiId() != null ? tenantSceneService.get(id).getUiId() : "");
        return Result.success();
    }

    @PostMapping("deletes")
    @ApiOperation(value = "批量删除")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(@Param("ids") String[] ids) {
        tenantSceneService.deleteByIds(ids);
        for (String str : ids) {
            tenantSceneResourceService.updateByTenantSceneId(str);
            //tenantSceneService.delete(tenantSceneService.get(str).getUiId() != null ? tenantSceneService.get(str).getUiId() : "");
        }
        return Result.success();
    }

    @PostMapping("editDo")
    @ApiOperation("修改")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(TenantScene tenantScene) {
        tenantScene.setShowPrice(tenantScene.getShowPrice() == null ? "0" : "1");
        tenantSceneService.saveOrUpdate(tenantScene);
        tenantSceneResourceService.deleteByTenantSceneId(tenantScene.getId());

        //背景音乐
        if (!Strings.isBlank(tenantScene.getMusicResourceArr())) {
            String musicJson = tenantScene.getMusicResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_music = JSONArray.fromObject(musicJson);
            for (int i = 0; i < jsonArray_music.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_music.get(i);
                TenantSceneResource sceneResource = new TenantSceneResource();
                sceneResource.setTenantSceneId(tenantScene.getId());
                sceneResource.setEventType(SceneResource.backgroundMusic);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResource.setSucceed(Boolean.parseBoolean(map.get("succeed")));
                tenantSceneResourceService.save(sceneResource);
            }
        }

        // 保存屏保资源
        if (!Strings.isBlank(tenantScene.getScreenSaverResourceArr())) {
            String screenSaverJson = tenantScene.getScreenSaverResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_boot = JSONArray.fromObject(screenSaverJson);
            for (int i = 0; i < jsonArray_boot.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_boot.get(i);
                TenantSceneResource sceneResource = new TenantSceneResource();
                sceneResource.setTenantSceneId(tenantScene.getId());
                sceneResource.setEventType(TenantSceneResource.screenSaverEvent);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setIndex(Strings.isBlank(map.get("index")) ? i : Integer.parseInt(map.get("index")));
                sceneResource.setPlayTime(map.get("playTime") == null ? "0" : map.get("playTime"));
                sceneResource.setSkipTime(map.get("skipTime") == null ? "0" : map.get("skipTime"));
                sceneResource.setSkip(map.get("skip") == null ? false : true);
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResource.setSucceed(Boolean.parseBoolean(map.get("succeed")));
                tenantSceneResourceService.save(sceneResource);
            }
        }

        // 保存开机资源
        if (!Strings.isBlank(tenantScene.getBootResourceArr())) {
            String bootJson = tenantScene.getBootResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_screenSaver = JSONArray.fromObject(bootJson);
            for (int i = 0; i < jsonArray_screenSaver.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_screenSaver.get(i);
                TenantSceneResource sceneResource = new TenantSceneResource();
                sceneResource.setTenantSceneId(tenantScene.getId());
                sceneResource.setEventType(TenantSceneResource.bootEvent);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setIndex(Strings.isBlank(map.get("index")) ? i : Integer.parseInt(map.get("index")));
                sceneResource.setPlayTime(map.get("playTime") == null ? "0" : map.get("playTime"));
                sceneResource.setSkipTime(map.get("skipTime") == null ? "0" : map.get("skipTime"));
                sceneResource.setSkip(map.get("skip") == null ? false : true);
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResource.setPhysicalBootResource(map.get("isPhysicalBootResource") == null ? false : true);
                sceneResource.setSucceed(Boolean.parseBoolean(map.get("succeed")));
                tenantSceneResourceService.save(sceneResource);
            }
        }
        // 保存关机资源
        if (!Strings.isBlank(tenantScene.getShutdownResourceArr())) {
            String shutdownJson = tenantScene.getShutdownResourceArr().replaceAll("&quot;", "\"");
            JSONArray jsonArray_shutdownJson = JSONArray.fromObject(shutdownJson);
            for (int i = 0; i < jsonArray_shutdownJson.size(); i++) {
                Map<String, String> map = (Map<String, String>) jsonArray_shutdownJson.get(i);
                TenantSceneResource sceneResource = new TenantSceneResource();
                sceneResource.setTenantSceneId(tenantScene.getId());
                sceneResource.setEventType(TenantSceneResource.shutdownEvent);
                sceneResource.setResourceId(map.get("resourceId"));
                sceneResource.setIndex(Strings.isBlank(map.get("index")) ? i : Integer.parseInt(map.get("index")));
                sceneResource.setPlayTime(map.get("playTime") == null ? "0" : map.get("playTime"));
                sceneResource.setSkipTime(map.get("skipTime") == null ? "0" : map.get("skipTime"));
                sceneResource.setSkip(map.get("skip") == null ? false : true);
                sceneResource.setAuthorize(map.get("isAuthorize") == null ? false : true);
                sceneResource.setSucceed(Boolean.parseBoolean(map.get("succeed")));
                tenantSceneResourceService.save(sceneResource);
            }
        }
        return Result.success();
    }


    /**
     * 更新
     *
     * @param id
     * @return
     */
    @PostMapping("update/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "更新")
    public Result update(@PathVariable("id") String id) {
        TenantScene scene = tenantSceneService.get(id);
        SceneVersionInfo info = sceneVersionInfoService.findSceneVersionInfoByBasicId(scene.getSceneId(), SceneVersionInfo.published);
        if (info != null) {
            SceneSaveVo sceneSaveVo = sceneBasicInfoService.findOne(info.getUiId());
            if (sceneSaveVo == null) return Result.error();
            TenantSceneVo tenantSceneVo = tenantSceneService.findOne(scene.getUiId());
            tenantSceneVo.setHashcode(sceneSaveVo.getAppUI().hashCode());
            tenantSceneVo.setAppUI(sceneSaveVo.getAppUI());
            tenantSceneService.editMongodb(tenantSceneVo);
        }
        return Result.success();
    }

    /**
     * 商户场景回显
     *
     * @param id
     * @return
     */
    @PostMapping("getSceneJson")
    @ResponseBody
    @ApiOperation("商户场景回显")
    @RequiresPermissions("sys.manager.unit")
    public Result getSceneJson(String id) {
        TenantScene tenantScene = tenantSceneService.get(id);
        TenantSceneVo tenantSceneVo = tenantSceneService.findOne(tenantScene.getUiId());
        return Result.success().addData(tenantSceneVo);
    }

    /**
     * 场景多语言
     *
     * @param id
     * @return
     */
    @PostMapping("/tenantLanguage")
    @ResponseBody
    @ApiOperation("场景多语言")
    @RequiresPermissions("sys.manager.unit")
    public Result tenantSceneLanguage(String id) {
        List<TenantLanguage> tenantLanguage = tenantLanguageService.findByTenantId(id);
        for (TenantLanguage t : tenantLanguage) {
            t.setLanguageName(languageDicService.findByCodeAndDelFlag(t.getLanguageDicCode()).getName());
        }
        return Result.success().addData(tenantLanguage);
    }

    @PostMapping("editSceneDo")
    @ApiOperation("编辑场景")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result editSceneDo(@RequestBody TenantSceneVo tenantSceneVo) {
        tenantSceneVo.setHashcode(tenantSceneVo.getAppUI().hashCode());
        tenantSceneService.editMongodb(tenantSceneVo);
        return Result.success();
    }

}
