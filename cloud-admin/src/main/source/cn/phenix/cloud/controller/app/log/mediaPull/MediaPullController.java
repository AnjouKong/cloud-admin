package cn.phenix.cloud.controller.app.log.mediaPull;

import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.admin.app.log.mediaChange.service.MediaPullLogService;
import cn.phenix.model.log.MediaPullLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;


@Controller
@Api(tags = "媒资转换日志")
@RequestMapping("/platform/operate/log")
public class MediaPullController extends BaseController {

    @Autowired
    private MediaPullLogService mediaPullLogService;

    @GetMapping("mediaChange")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "媒资转换日志首页")
    public String index() {
        return "app/log/mediaChange/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "媒资转换日志首页数据加载")
    public PageTable<MediaPullLog> data(MediaPullLog mediaPullLog,String sstartDate,String estartDate, @Param("draw") Integer draw, DataTableParameter parameter) throws ParseException {
        PageTable<MediaPullLog> pageTable = mediaPullLogService.findPage(parameter,mediaPullLog,sstartDate,estartDate);
        pageTable.setDraw(draw);
        return pageTable;
    }
}
