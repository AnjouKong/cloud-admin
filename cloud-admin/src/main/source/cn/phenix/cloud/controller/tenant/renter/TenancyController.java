package cn.phenix.cloud.controller.tenant.renter;

import cn.phenix.cloud.admin.app.cms.service.CmsAdvertiseService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourceService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.media.homePage.service.MediaHomePageService;
import cn.phenix.cloud.admin.app.media.media.service.MediaLevelService;
import cn.phenix.cloud.admin.app.scene.basic.service.SceneBasicInfoService;
import cn.phenix.cloud.admin.app.scene.other.service.SceneLanguageService;
import cn.phenix.cloud.admin.sys.service.SysApiService;
import cn.phenix.cloud.admin.sys.service.SysAreaService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceUserExtendService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceUserGroupService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceUserService;
import cn.phenix.cloud.admin.tenant.pay.config.CacheConf;
import cn.phenix.cloud.admin.tenant.pay.vo.ResponseJson;
import cn.phenix.cloud.admin.tenant.renter.service.*;
import cn.phenix.cloud.admin.tenant.vip.service.PackageTenantService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.cloud.core.utils.JsonUtils;
import cn.phenix.cloud.core.utils.MD5Util;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.rpc.cache.vo.CacheVo;
import cn.phenix.model.app.cms.*;
import cn.phenix.model.app.media.MediaHomePage;
import cn.phenix.model.app.media.MediaLevel;
import cn.phenix.model.app.scene.SceneLanguage;
import cn.phenix.model.sys.SysApi;
import cn.phenix.model.sys.SysArea;
import cn.phenix.model.tenant.device.DeviceUser;
import cn.phenix.model.tenant.device.DeviceUserGroup;
import cn.phenix.model.tenant.renter.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户管理
 */
@Controller
@RequestMapping("platform/operate/tenant")
@Api(tags = "租户管理")
@RequiresPermissions("sys.manager.unit")
public class TenancyController extends BaseController {
    @Autowired
    private TenancyService tenantService;
    @Autowired
    private MediaLevelService mediaLevelService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private TenancyInfoService tenancyInfoService;
    @Autowired
    private TenantOfficeService tenantOfficeService;
    @Autowired
    private SysApiService sysApiService;
    @Autowired
    private CacheConf cacheConf;
    @Autowired
    private LanguageDicService languageDicService;
    @Autowired
    private TenantLanguageService tenantLanguageService;
    @Autowired
    private SceneBasicInfoService sceneBasicInfoService;
    @Autowired
    private TenantSceneService tenantSceneService;
    @Autowired
    private SceneLanguageService sceneLanguageService;
    @Autowired
    private CmsResourceService cmsResourceService;
    @Autowired
    private TenantSceneResourceService tenantSceneResourceService;
    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;
    @Autowired
    private CmsAdvertiseService cmsAdvertiseService;
    @Autowired
    private MediaHomePageService mediaHomePageService;
    @Autowired
    private PackageTenantService packageTenantService;
    @Autowired
    private DeviceUserService deviceUserService;
    @Autowired
    private DeviceUserGroupService deviceUserGroupService;
    @Autowired
    private DeviceUserExtendService deviceUserExtendService;

    /**
     * 首页列表
     */

    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(Model model) {
        return "tenant/tenant/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation("首页数据加载")
    public PageTable<Tenancy> data(Tenancy tenancy, TenancyInfo tenancyInfo, Integer draw, DataTableParameter parameter) {

        PageTable<Tenancy> pageTable = tenantService.findPage(parameter, tenancy, tenancyInfo);


        pageTable.setDraw(draw);

        return pageTable;
    }

    @RequestMapping("add")
    @ApiOperation("添加页面跳转")
    public String add(Model model, String unitid) {
        model.addAttribute("obj", StringUtils.isBlank(unitid) ? null : tenantService.get(unitid));
        model.addAttribute("languageDic", languageDicService.findLanguageDic());
        return "tenant/tenant/add";
    }

    @PostMapping(value = "/addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    @Transactional
    public Result addDo(Tenancy tenancy, TenancyInfo tenancyInfo) {
        if (tenancy != null) {
            Tenancy u = tenantService.findByTenancyCode(tenancy.getTenancyCode());
            if (u != null)
                return Result.error("商户标识已存在");
        }
        //扩展表和主表使用同一个ID
        String id = IdGen.uuid();
        //主表插入
        tenancy.setId(id);
        tenancy.setDelFlag("0");
        tenancy.setTenantId(id);
        //扩展表插入
        tenancyInfo.setId(id);
        tenancyInfo.setDelFlag("0");
        tenancyInfo.setTenantId(id);
        //扩展表生成设备信息
        String appId = IdGen.randomBase62(10);
        String appSecret = IdGen.randomBase62(16);
        tenancyInfo.setAppId(appId);
        tenancyInfo.setAppSecret(appSecret);

        String areaName;
        SysArea area = sysAreaService.get(tenancy.getAreaId());
        if ("4".equalsIgnoreCase(area.getType())) {
            areaName = area.getParent().getParent().getName() + area.getParent().getName() + area.getName();
        } else if ("3".equalsIgnoreCase(area.getType())) {
            areaName = area.getParent().getName() + area.getName();
        } else {
            areaName = area.getName();
        }
        tenancy.setAreaName(areaName);
        tenantService.save(tenancy);
        tenancyInfoService.save(tenancyInfo);
        //商户组织初始化，为了商户端组织使用
        TenantOffice tenantOffice = new TenantOffice();
        tenantOffice.setTenantId(tenancy.getId());
        tenantOffice.setName(tenancy.getTenancyName());
        tenantOffice.setType("1");
        tenantOffice.setUseable("1");
        tenantOfficeService.save(tenantOffice);
        //商户语言
        for (String code : tenancy.getLanguageDic()) {
            TenantLanguage l = new TenantLanguage();
            l.setLanguageDicCode(code);
            l.setTenantId(tenancy.getTenantId());
            tenantLanguageService.save(l);
        }
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "预览详情")
    public String detail(@PathVariable("id") String id, Model model, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            Tenancy tenancy = tenantService.get(id);
            //扩展表信息
            req.setAttribute("tenancyInfo", tenancyInfoService.get(id));
            model.addAttribute("obj", tenancy);
            //2018年1月12日17:03:13 信息整合
            model.addAttribute("languageList", tenantLanguageService.findByTenantId(id));//多语言
            if (tenancy != null && StringUtils.isNotEmpty(tenancy.getLevelId())) {
                model.addAttribute("level", mediaLevelService.get(tenancy.getLevelId()));//媒资级别
            }
            model.addAttribute("userGroup", deviceUserGroupService.findByDelFlagAndTenantId("0", id));//终端组信息
            model.addAttribute("sceneList", tenantSceneService.findByDelFlagAndTenantId("0", id));//场景信息
            model.addAttribute("tenantPackage", packageTenantService.getPTinfoByTenantId(id));//套餐包信息
            return "tenant/tenant/detail";
        } else {
            return null;
        }
    }

    @PostMapping("/enable/{id}")
    @ResponseBody
    @ApiOperation("状态启用")
    @RequiresPermissions("sys.manager.unit")
    public Result enable(@PathVariable("id") String id) {
        Tenancy tenancy = tenantService.get(id);
        tenancy.setStatus("1");
        tenantService.save(tenancy);
        return Result.success();
    }

    @PostMapping("/disable/{id}")
    @ApiOperation("状态禁用")
    @RequiresPermissions("sys.manager.unit")
    @ResponseBody
    public Result disable(@PathVariable("id") String id, HttpServletRequest req) {
        Tenancy tenancy = tenantService.get(id);
        tenancy.setStatus("0");
        tenantService.save(tenancy);
        return Result.success();
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        List<DeviceUser> list = deviceUserService.findByTenantIdAndDelFlag(id);
        if (list != null && list.size() > 0)
            return Result.error("当前商户，设备组有设备正在使用，请先删除设备。");
        tenantService.deleteById(id);
        //扩展表删除
        tenancyInfoService.deleteById(id);
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        for (String id : ids) {
            List<DeviceUser> list = deviceUserService.findByTenantIdAndDelFlag(id);
            if (list != null && list.size() > 0)
                return Result.error("已选择的商户，设备组有设备正在使用，请先删除设备。");
        }
        tenantService.deleteByIds(ids);
        //扩展表删除
        tenancyInfoService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model, HttpServletRequest req) {
        Tenancy tenancy = tenantService.get(id);
        //扩展表信息
        req.setAttribute("tenancyInfo", tenancyInfoService.get(id));
        model.addAttribute("obj", tenancy);
        return "tenant/tenant/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(Tenancy tenancy, TenancyInfo tenancyInfo, String oldTenancyCode) {
        if (!Strings.sBlank(oldTenancyCode).equals(tenancy.getTenancyCode())) {
            Tenancy u = tenantService.findByTenancyCode(tenancy.getTenancyCode());
            if (u != null)
                return Result.error("商户名已存在");
        }
        String areaName;
        SysArea area = sysAreaService.get(tenancy.getAreaId());
        if ("4".equalsIgnoreCase(area.getType())) {
            areaName = area.getParent().getParent().getName() + area.getParent().getName() + area.getName();
        } else if ("3".equalsIgnoreCase(area.getType())) {
            areaName = area.getParent().getName() + area.getName();
        } else {
            areaName = area.getName();
        }
        tenancy.setAreaName(areaName);
        tenantService.save(tenancy);
        //扩展表信息
        tenancyInfo.setTenantId(tenancy.getId());
        //扩展表生成设备信息
        if (StringUtils.isEmpty(tenancyInfo.getAppId()) || StringUtils.isEmpty(tenancyInfo.getAppSecret())) {
            String appId = IdGen.randomBase62(10);
            String appSecret = IdGen.randomBase62(16);
            tenancyInfo.setAppId(appId);
            tenancyInfo.setAppSecret(appSecret);
        }
        tenancyInfoService.save(tenancyInfo);
        return Result.success();
    }

    @GetMapping("/treeTenancy")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("树加载")
    public List<Map<String, Object>> treeTenancy() {
        List<Tenancy> list = tenantService.findByStatus("1");
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Tenancy tenancy : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", tenancy.getId());
            obj.put("text", tenancy.getTenancyName());
            tree.add(obj);
        }
        return tree;
    }

    @GetMapping("level/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("级别")
    public String level(@PathVariable("id") String id, Model model) {
        MediaLevel level = new MediaLevel();
        level.setId("0");
        if (!Strings.isBlank(tenantService.get(id).getLevelId())) {
            level = mediaLevelService.get(tenantService.get(id).getLevelId());
        }
        if (level == null) {     //防止Id为空在查询时跳过此条件
            level = new MediaLevel();
            level.setId("0");
            level.setLevelName("无");
        }
        model.addAttribute("level", level);
        model.addAttribute("tenantId", id);
        return "tenant/tenant/level";
    }

    @GetMapping("selectLevel")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择级别页面")
    public String selectLevel() {
        return "tenant/tenant/selectLevel";
    }

    @RequestMapping(value = "pushLevel")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("确认选择级别")
    public Result pushLevel(@Param("id") String id, @Param("levelId") String levelId) {
        Tenancy u = tenantService.get(id);
        u.setLevelId(levelId);
        tenantService.saveOrUpdate(u);
        return Result.success();
    }


    @RequestMapping("/group/{id}")
    @ApiOperation("设备组列表")
    public String group(@PathVariable("id") String id, HttpServletRequest req) {
        req.setAttribute("tenantId", id);
        return "tenant/tenant/deviceUserGroup";
    }

    @RequestMapping("/addDeviceUserGroup/{id}")
    @ApiOperation("添加设备组")
    public String addDeviceUserGroup(@PathVariable("id") String id, HttpServletRequest req) {
        req.setAttribute("tenantId", id);
        return "tenant/tenant/addDeviceUserGroup";
    }

    @RequestMapping("/addDeviceUserGroupDo")
    @ResponseBody
    @ApiOperation("添加设备组")
    public Result addDeviceUserGroupDo(DeviceUserGroup group) {
        deviceUserGroupService.save(group);
        return Result.success();
    }

    @RequestMapping("/sceneRecord")
    @ApiOperation("设备组列表")
    public String sceneRecord(@Param("id") String id, @Param("tenantId") String tenantId, HttpServletRequest req) {
        req.setAttribute("groupId", id);
        req.setAttribute("tenantId", tenantId);
        return "tenant/tenant/sceneRecord";
    }

    @RequestMapping("/language/{id}")
    @ApiOperation("语言页面")
    public String language(@PathVariable("id") String id, Model model) {
        model.addAttribute("tenantId", id);
        model.addAttribute("languageDic", languageDicService.findLanguageDic());
        model.addAttribute("language", tenantLanguageService.findByTenantId(id));
        return "tenant/tenant/language";
    }

    @RequestMapping(value = "languageDo")
    @ResponseBody
    @ApiOperation(value = "设置多语言")
    @RequiresPermissions("sys.manager.unit")
    public Result languageDo(TenantLanguage language) {
        tenantLanguageService.deleteByTenantId(language.getTenantId());
        for (String code : language.getLanguageDic()) {
            TenantLanguage l = new TenantLanguage();
            l.setLanguageDicCode(code);
            l.setTenantId(language.getTenantId());
            tenantLanguageService.save(l);
        }
        return Result.success();
    }

    @GetMapping("/editSceneBasicInfo/{id}")
    @ApiOperation("修改场景信息")
    @RequiresPermissions("sys.manager.unit")
    public String editSceneBasicInfo(@PathVariable("id") String id, Model model) {
        TenantScene tenantScene = tenantSceneService.get(id);
        if (!Strings.isBlank(tenantScene.getMediaHomePageId())) {
            MediaHomePage homePage = mediaHomePageService.get(tenantScene.getMediaHomePageId());
            tenantScene.setHomePageName(homePage == null ? "" : homePage.getHomePageName());
        }
        tenantScene.setTemplateName(sceneBasicInfoService.get(tenantScene.getSceneId()) == null ? null : sceneBasicInfoService.get(tenantScene.getSceneId()).getSceneName());
        List<SceneLanguage> sceneLanguage = sceneLanguageService.findBySceneId(tenantScene.getSceneId());
        String languageName = "";
        for (SceneLanguage l : sceneLanguage) {
            languageName += l.getLanguageDic().getName() + ",";
        }
        tenantScene.setTemplateLanguage(!Strings.isBlank(languageName) ? languageName.substring(0, languageName.length() - 1) : "");

        List<TenantSceneResource> tenantSceneResourceList = tenantSceneResourceService.findByTenantSceneIdAndDelFlag(id);
        for (TenantSceneResource resource : tenantSceneResourceList) {
            CmsResource cmsResource = cmsResourceService.get(resource.getResourceId());
            if (cmsResource == null)
                continue;
            resource.setResourceName(cmsResource.getName());
            resource.setResourceType(cmsResource.getResourceType().getText());

            List<CmsResourcesLanguage> language = cmsResourcesLanguageService.findByResourceId(resource.getResourceId());
            String name = "";
            for (CmsResourcesLanguage c : language) {
                if (c.getLanguageDic() == null)
                    continue;
                name += c.getLanguageDic().getName() + ",";
            }
            resource.setLanguageName(!Strings.isBlank(name) ? name.substring(0, name.length() - 1) : "");

            String modelName = getModelName(cmsResource.getResourceType().name());
            if (modelName != null) {
                resource.setResource(cmsResource.getId() + "/" + modelName + "/");
            } else {
                resource.setResource("");
            }
            //验证是否为视频
            if (cmsResource.getResourceType().name().equals("video")) {
                resource.setVideo(true);
                continue;
            }
            if (cmsResource.getResourceType().name().equals("ad")) {
                CmsAdvertise adv = cmsAdvertiseService.get(resource.getResourceId());
                if ("video".equals(adv.getFileType())) {
                    resource.setVideo(true);
                    continue;
                }
            }

        }

        model.addAttribute("obj", tenantScene);
        model.addAttribute("resourceList", tenantSceneResourceList);
        model.addAttribute("modelName", TenantScene.modelName);

        return "tenant/tenant/scene/edit";
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

    @RequestMapping("/scene/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("场景页面跳转")
    public String scene(@PathVariable("id") String id, Model model) {
        model.addAttribute("tenantId", id);
        return "tenant/tenant/scene/index";
    }

    @GetMapping("/editScene")
    @ApiOperation("编辑场景")
    @RequiresPermissions("sys.manager.unit")
    public String editScene(@Param("id") String id, @Param("tenantId") String tenantId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("tenantId", tenantId);
        TenantScene vo = tenantSceneService.get(id);
        String style = vo == null || vo.getStyle().equals(TenantScene.style_delayering) ? "configure" : "configure_suspension";
        return "tenant/tenant/scene/" + style;
    }

    @GetMapping("/selectDeviceUserGroup")
    @ApiOperation("编辑场景")
    @RequiresPermissions("sys.manager.unit")
    public String selectDeviceUserGroup(@Param("id") String id, @Param("tenantId") String tenantId, Model model) {
        model.addAttribute("tenantSceneId", id);
        model.addAttribute("tenantId", tenantId);
        return "tenant/tenant/scene/selectDeviceUserGroup";
    }

    @PostMapping("/cache/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ResponseBody
    @ApiOperation("清除缓存")
    public Result cache(@PathVariable("id") String tenantId, String cacheOption) {
        ResponseJson json = cacheClear(tenantId);
        if (json.getHead().getCode() == 200) {
            return Result.success();
        } else {
            return Result.error(String.valueOf(json.getHead().getCode()));
        }
    }

    @GetMapping("/cacheAll")
    @RequiresPermissions("sys.manager.unit")
    @ResponseBody
    @ApiOperation("清除全部缓存")
    public Result cacheAll() {
        List<Tenancy> tenancyList = tenantService.findByStatus("1");
        for (Tenancy tenancy : tenancyList) {
            cacheClear(tenancy.getId());
        }
        return Result.success();
    }

    private void packageCacheVo(String id, CacheVo cacheVo) {
        SysApi api = sysApiService.findByDelFlagAndAppId("41ef33dab5"); //获取终端
        cacheVo.setAppId(api.getAppId());
        cacheVo.setTenantId(id);
        cacheVo.setTimestamp(System.currentTimeMillis() + "");
        StringBuffer sb = new StringBuffer();
        sb.append(cacheVo.getAppId()).append(cacheVo.getTimestamp()).append(cacheVo.getTenantId()).append("#").append(api.getAppSecret()); //混淆码
        cacheVo.setSignature(MD5Util.getMD5String(sb.toString()));

    }

    private ResponseJson cacheClear(String tenantId) {
        //封装参数
        CacheVo cacheVo = new CacheVo();
        this.packageCacheVo(tenantId, cacheVo);
        //创建一个头部对象
        HttpHeaders headers = new HttpHeaders();
        //设置contentType
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        //设置我们的请求信息，第一个参数为请求Body,第二个参数为请求头信息
        HttpEntity<String> strEntity = new HttpEntity(JsonUtils.writeObjectToJson(cacheVo), headers);
        RestTemplate restTemplate = new RestTemplate();
        //请求接口
        String restJson = restTemplate.postForObject(cacheConf.getCacheUrl(), strEntity, String.class, cacheVo);
        ResponseJson json = JsonUtils.readObjectByJson(restJson, ResponseJson.class);
        return json;

    }


    @RequestMapping("/channel/{id}")
    @ApiOperation("信号源跳转")
    public String channel(@PathVariable("id") String id, Model model) {
        model.addAttribute("tenantId", id);
        return "tenant/channel/index";
    }

    @RequestMapping("/proxy/{id}")
    @ApiOperation("代理配置跳转")
    public String proxy(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        Tenancy tenancy = tenantService.get(id);
        model.addAttribute("tenancy", tenancy);
        model.addAttribute("hasPrivate", "on".equals(tenancy.getHasPrivate()) ? "checked" : "");
        return "tenant/tenant/proxy";
    }

    @PostMapping("/editProxyDo")
    @ResponseBody
    @ApiOperation("保存代理配置")
    @RequiresPermissions("sys.manager.unit")
    public Result editProxyDo(Tenancy deviceUserExtends) {
        deviceUserExtendService.updateDeviceUserProxy(deviceUserExtends);
        return Result.success();
    }

}
