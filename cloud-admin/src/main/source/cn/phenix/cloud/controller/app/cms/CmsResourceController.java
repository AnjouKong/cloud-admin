package cn.phenix.cloud.controller.app.cms;


import cn.phenix.cloud.admin.app.cms.service.CmsResourceService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.model.app.cms.*;
import cn.phenix.model.mechanism.AttMain;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("platform/cms/resource")
@Api(tags = "资源管理")
@RequiresPermissions("sys.manager.unit")
public class CmsResourceController extends BaseController {

    @Autowired
    private CmsResourceService cmsResourceService;
    @Autowired
    private AttMainService attMainService;


    @PostMapping(value = "getResourceUrl")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result getResourceUrl(String modelId, String eventType, String language) {
        if (Strings.isBlank(eventType) || Strings.isBlank(modelId) || Strings.isBlank(language)) return null;
        String url = "";
        String modelName = getModelName(eventType);
        if (Strings.isBlank(modelName)) return null;

        List<AttMain> attMainList = attMainService.getAttMain(modelId, modelName, language);
        for (AttMain main : attMainList) {
            url += main.getFdFilePath() + ",";
        }
        if (!Strings.isBlank(url)) url = url.substring(0, url.length() - 1);
        return new Result().addData(url);
    }

    private String getModelName(String eventType) {
        switch (eventType) {
            case "advEvent":
                return CmsAdvertise.modelName;
            case "appEvent":
                return CmsApp.modelName;
            case "imageEvent":
                return CmsImg.modelName;
            case "imageListEvent":
                return CmsImgCollection.modelName;
            case "videoEvent":
                return CmsVideo.modelName;
            case "HtmlEvent":
                return CmsHtml.modelName;
            case "":
                return null;
        }

        return null;
    }


}
