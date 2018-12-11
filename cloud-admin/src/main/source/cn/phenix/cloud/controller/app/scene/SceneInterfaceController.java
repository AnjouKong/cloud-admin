package cn.phenix.cloud.controller.app.scene;

import cn.phenix.cloud.admin.app.cms.service.CmsResourceService;
import cn.phenix.cloud.admin.app.media.media.service.MediaSeriesService;
import cn.phenix.cloud.admin.app.scene.config.ResourceConf;
import cn.phenix.cloud.admin.app.scene.config.VodConf;
import cn.phenix.cloud.admin.app.scene.vo.ResponseJson;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.admin.sys.service.SysApiService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.JsonUtils;
import cn.phenix.cloud.core.utils.MD5Util;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.common.rest.HttpClientUtil;
import cn.phenix.model.app.cms.*;
import cn.phenix.model.app.media.MediaSeries;
import cn.phenix.model.mechanism.AttMain;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Api(tags = "场景接口")
@RequestMapping("platform/scene/interface")
public class SceneInterfaceController extends BaseController {

    @Autowired
    private AttMainService attMainService;
    @Autowired
    private VodConf vodConf;
    @Autowired
    private ResourceConf resourceConf;
    @Autowired
    private SysApiService sysApiService;
    @Autowired
    private CmsResourceService cmsResourceService;
    @Autowired
    private MediaSeriesService mediaSeriesService;

    /**
     * 开机资源
     *
     * @return
     */
    @GetMapping("/bootResource")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "获取开机资源")
    public Result bootResource() throws Exception {
        Map<String, String> head = new HashMap<>();
        head.put("token", getToken());
        String restJson = HttpClientUtil.sendGetByRest(resourceConf.getBootResourceAPI(), head);
        ResponseJson responseJson = JsonUtils.readObjectByJson(restJson, ResponseJson.class);
        if (responseJson.getHead().getCode() == 200) {   // 成功
            JSONObject json = JSON.parseObject(restJson);
            JSONObject body = JSON.parseObject(json.getString("body"));
            return Result.success().addData(body);
        }
        return Result.error(responseJson.getHead().getMsg());
    }

    /**
     * 屏保资源
     *
     * @return
     */
    @GetMapping("/screenProtector")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "获取屏保资源")
    public Result screenProtector() throws Exception {
        Map<String, String> head = new HashMap<>();
        head.put("token", getToken());
        String restJson = HttpClientUtil.sendGetByRest(resourceConf.getScreenProtectorAPI(), head);
        ResponseJson responseJson = JsonUtils.readObjectByJson(restJson, ResponseJson.class);
        if (responseJson.getHead().getCode() == 200) {   // 成功
            JSONObject json = JSON.parseObject(restJson);
            JSONObject body = JSON.parseObject(json.getString("body"));
            return Result.success().addData(body);
        }
        return Result.error();
    }

    /**
     * 媒资分类
     *
     * @return
     */
    @GetMapping("/mediaCategory")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "获取媒资分类")
    public Result mediaCategory() throws Exception {
        Map<String, String> head = new HashMap<>();
        head.put("token", getToken());
        String restJson = HttpClientUtil.sendPostByRest(vodConf.getCategoryHandler(), head, null);
        ResponseJson responseJson = JsonUtils.readObjectByJson(restJson, ResponseJson.class);
        if (responseJson.getHead().getCode() == 200) {   // 成功
            JSONObject json = JSON.parseObject(restJson);
            com.alibaba.fastjson.JSONArray body = JSON.parseArray(json.getString("body"));
            return Result.success().addData(body);
        }
        return Result.error();
    }

    /**
     * 媒资列表
     *
     * @return
     */
    @GetMapping("/mediaList/{categoryId}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "获取媒资列表")
    public Result mediaList(@PathVariable("categoryId") String categoryId) throws Exception {
        Map<String, String> head = new HashMap<>();
        head.put("token", getToken());

        MediaListVo listVo = new MediaListVo();
        listVo.setCategoryId(categoryId);
        listVo.setPageNo(1);
        listVo.setPageSize(100);

        String restJson = HttpClientUtil.sendPostByRest(vodConf.getSearchHandler(), head, listVo);

        ResponseJson responseJson = JsonUtils.readObjectByJson(restJson, ResponseJson.class);
        if (responseJson.getHead().getCode() == 200) {   // 成功
            JSONObject json = JSON.parseObject(restJson);
            JSONObject body = JSON.parseObject(json.getString("body"));
            //com.alibaba.fastjson.JSONArray media = JSON.parseArray(body.getString("media"));

            return Result.success().addData(body);
        }
        return Result.error();
    }

    /**
     * 媒资详情
     *
     * @return
     */
    @GetMapping("/mediaDetail/{seriesId}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "获取媒资详情")
    public Result mediaDetail(@PathVariable("seriesId") String seriesId) throws Exception {
        Map<String, String> head = new HashMap<>();
        head.put("token", getToken());

        String restJson = HttpClientUtil.sendGetByRest(vodConf.getMediaDetailController() + seriesId, head);
        ResponseJson responseJson = JsonUtils.readObjectByJson(restJson, ResponseJson.class);

        if (responseJson.getHead().getCode() == 200) {   // 成功
            JSONObject json = JSON.parseObject(restJson);
            JSONObject body = JSON.parseObject(json.getString("body"));
            return Result.success().addData(body);
        }
        return Result.error();
    }

    /**
     * 模拟获取token
     *
     * @return
     */
    private String getToken() throws Exception {
        String time = System.currentTimeMillis() + "";
        Map<String, Object> loginVo = new HashMap<>();
        loginVo.put("appId", "b79d5cb8c3");
        loginVo.put("deviceId", vodConf.getDeviceId());
        loginVo.put("timestamp", time);
        loginVo.put("versionId", "v1");

        StringBuilder sb = new StringBuilder();
        sb.append("b79d5cb8c3").append(time).append(vodConf.getDeviceId()).append("v1").append("#")
                .append(sysApiService.findByDelFlagAndAppId("b79d5cb8c3").getAppSecret());//混淆码，需要数据库取
        String decrypt = MD5Util.getMD5String(sb.toString());
        loginVo.put("signature", decrypt);
        String restJson = HttpClientUtil.sendPostByRest(vodConf.getGetTokenUrl(), null, loginVo);
        ResponseJson responseJson = JsonUtils.readObjectByJson(restJson, ResponseJson.class);
        if (responseJson.getHead().getCode() == 200) {   // 成功
            JSONObject json = JSON.parseObject(restJson);
            JSONObject body = JSON.parseObject(json.getString("body"));
            return body.getString("deviceToken");
        }
        return null;
    }

    /**
     * 上传背景图片
     */
    @PostMapping(value = "addBackgroundPic")
    @ResponseBody
    @ApiOperation(value = "背景图片")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(AttMainForm attMainForm) {
        String id = "";
        for (AttMain a : attMainForm.getAttMains1()) {
            if (!Strings.isBlank(a.getFdKey())) id = a.getId();
        }
        return Result.success().addData(id);
    }


    @GetMapping("/pic")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "查看图片")
    public String pic(String id, String type, Model model) {
        List<AttMain> list = attMainService.getAttMain(id, typeToModelName(type), "pic");
        model.addAttribute("pic", list);
        return "app/scene/basic/pic";
    }

    /**
     * 根据图片Id查询背景图片
     *
     * @param id modelId
     * @return json
     */
    @RequestMapping("/getPicUrl")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "获取图片")
    public Result detail(String id) {
        return Result.success().addData(attMainService.get(id) != null ? attMainService.get(id).getFdFilePath() : "");
    }

    /**
     * 返回资源name
     *
     * @param id   modelId
     * @param type 类型
     * @return 图片集合
     */
    @RequestMapping("getResourceName")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "获取资源名称")
    public Result getResourceName(@Param("id") String id, @Param("type") String type) {
        String name;
        if ("CustomEvent".equals(type)) {
            MediaSeries mediaSeries = mediaSeriesService.get(id);
            name = mediaSeries == null ? "" : mediaSeries.getSeriesName();
        } else {
            CmsResource resource = cmsResourceService.get(id);
            name = resource == null ? "" : resource.getName();
        }
        return new Result().addData(name);
    }

    /**
     * 将类型转化为modelName
     *
     * @param type 类型
     * @return modelName
     */
    private String typeToModelName(String type) {
        if ("img".equalsIgnoreCase(type))
            return CmsImg.modelName;
        else if ("app".equalsIgnoreCase(type))
            return CmsApp.modelName;
        else if ("article".equalsIgnoreCase(type))
            return CmsHtmlArticle.modelName;
        else if ("imgCollection".equalsIgnoreCase(type))
            return CmsImgCollection.modelName;
        else return null;
    }

}

@Getter
@Setter
class MediaListVo {
    public String categoryId;
    public int pageNo;
    public int pageSize;
}