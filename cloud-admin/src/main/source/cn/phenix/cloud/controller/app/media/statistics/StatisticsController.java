package cn.phenix.cloud.controller.app.media.statistics;

import cn.phenix.cloud.admin.app.media.media.service.MediaLevelService;
import cn.phenix.cloud.admin.app.media.statistics.service.StatisticsService;
import cn.phenix.cloud.admin.app.media.tag.service.MediaTagService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.model.app.media.MediaLevel;
import cn.phenix.model.app.media.MediaSeries;
import cn.phenix.model.app.media.MediaTag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@Api(tags = "统计")
@RequestMapping("platform/media/statistics")
public class StatisticsController extends BaseController {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private MediaTagService mediaTagService;
    @Autowired
    private MediaLevelService mediaLevelService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index(HttpServletRequest req) {
        List<MediaTag> catalog = mediaTagService.findByDelFlag("0");
        req.setAttribute("catalog",catalog);
        List<MediaLevel> levelObj = mediaLevelService.findByDelFlag("0");
        req.setAttribute("levelObj",levelObj);
        return "app/media/statistics/index";
    }

    /**
     * 查询数据
     */
    @ResponseBody
    @RequestMapping("search")
    @ApiOperation(value = "查询数据")
    @RequiresPermissions("sys.manager.unit")
    public Integer statistics(MediaSeries mediaSeries,@Param("tagId") String tagId){
        try {
            int num=  statisticsService.search(mediaSeries,tagId).size();
            return num;
        } catch (Exception e) {
            return 0;
        }
    }
}
