package cn.phenix.cloud.controller.app.newMedia.statistics;

import cn.phenix.cloud.admin.app.newMedia.statistics.service.NewStatisticsService;
import cn.phenix.cloud.admin.app.newMedia.tag.service.NewMediaTagService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.model.app.newMedia.NewMediaSeries;
import cn.phenix.model.app.newMedia.NewMediaTag;
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
@RequestMapping("platform/newMedia/statistics")
public class NewStatisticsController extends BaseController {

    @Autowired
    private NewStatisticsService statisticsService;
    @Autowired
    private NewMediaTagService mediaTagService;


    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index(HttpServletRequest req) {
        List<NewMediaTag> catalog = mediaTagService.findByDelFlag("0");
        req.setAttribute("catalog",catalog);
        return "app/newMedia/statistics/index";
    }

    /**
     * 查询数据
     */
    @ResponseBody
    @RequestMapping("search")
    @ApiOperation(value = "查询数据")
    @RequiresPermissions("sys.manager.unit")
    public Integer statistics(NewMediaSeries mediaSeries, @Param("tagId") String tagId){
        try {
            int num=  statisticsService.search(mediaSeries,tagId).size();
            return num;
        } catch (Exception e) {
            return 0;
        }
    }
}
