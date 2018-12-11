package cn.phenix.cloud.controller.app.media.tag;


import cn.phenix.cloud.admin.app.media.tag.service.MediaTagService;
import cn.phenix.cloud.admin.app.vo.media.MediaSeriesTagVo;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.media.MediaTag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Created by fxq
 */
@Controller
@Api(tags = "分类排序")
@RequestMapping("/platform/media/tag/sort")
public class MediaTagSortController extends BaseController {
    @Autowired
    private MediaTagService mediaTagService;

    @RequestMapping("sort")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("分类排序页面跳转")
    public String sort(String levelId, Model model) {
        List<MediaTag> firstMenus = mediaTagService.findFirstMenus(levelId, null);
        model.addAttribute("firstMenus", firstMenus);
        model.addAttribute("levelId", levelId);
        return "app/media/tag/sort";
    }

    @RequestMapping("getSubjectTag")
    @ResponseBody
    @ApiOperation("获取二级专题菜单")
    public List<MediaSeriesTagVo> getSubjectTag(@Param("levelId") String levelId, @Param("tagId") String tagId) {
        return mediaTagService.getSecondSubjectMenu(levelId, tagId);
    }

    @RequestMapping("sortDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("分类排序")
    public Result sortDo(@Param("ids") String ids) {
        //2017年12月29日10:24:38 老排序未考虑加级别后的情况，现在排序应该在级别内改变，并不是全部更新位置
        String[] menuIds = ids.split(",");
        int i = 0;
        int n = 0;

        //2018年1月16日13:52:48 跟随页面更改排序规则为标签内排序
        for (String s : menuIds) {
            if (!Strings.isBlank(s)) {
                if (s.contains("#")) {
                    String[] subjectTag = s.split("#");//0:seriesId,1:tagId
                    mediaTagService.updateSubjectLocation(i, subjectTag[0], subjectTag[1]);
                    i++;
                } else {
                    mediaTagService.updateByUpdate(Parameter.create().insert("index", n), Parameter.create().insert("id", s));
                    n++;
                }

            }
        }
        return Result.success();
    }

}
