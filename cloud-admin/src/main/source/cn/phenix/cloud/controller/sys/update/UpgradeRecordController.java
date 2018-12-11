package cn.phenix.cloud.controller.sys.update;

import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.update.service.LauncherVersionService;
import cn.phenix.cloud.admin.update.service.UpgradeRecordService;
import cn.phenix.cloud.admin.update.service.UpgradeStrategyService;
import cn.phenix.cloud.base.utils.StringUtil;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.mechanism.AttMain;
import cn.phenix.model.upgrade.LauncherVersion;
import cn.phenix.model.upgrade.UpgradeRecord;
import cn.phenix.model.upgrade.UpgradeStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Api(tags = "系统升级日志")
@RequestMapping("platform/sys/upgrade/record")
public class UpgradeRecordController extends BaseController {

    @Autowired
    private UpgradeRecordService upgradeRecordService;
    @Autowired
    private UpgradeStrategyService strategyService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private LauncherVersionService launcherVersionService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "sys/update/record/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<UpgradeRecord> data(UpgradeRecord upgradeRecord, String sstartDate, String estartDate, @Param("draw") Integer draw, DataTableParameter parameter) {
        PageTable<UpgradeRecord> pageTable = upgradeRecordService.findPage(parameter,upgradeRecord,sstartDate,estartDate);

        for(int i=0;i<pageTable.getData().size();i++){
            UpgradeStrategy upgradeStrategy = strategyService.get(pageTable.getData().get(i).getUpgradeStrategyId());
            if(upgradeStrategy != null){
                pageTable.getData().get(i).setUpgradeStrategyId(upgradeStrategy.getName());
            }
        }
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("versionDetail")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("版本详情")
    public String versionDetail(String id, Model model) {
        List<AttMain> attMainList =attMainService.getAttMain(id, LauncherVersion.MODEL_NAME, "version");
        model.addAttribute("obj",  launcherVersionService.get(id));
        model.addAttribute("attMain",attMainList.size()>0?attMainList.get(0):null);
        return "sys/update/record/versionDetail";
    }
    @GetMapping("detail")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("查看详情")
    public String detail(String id, Model model) {
        if(StringUtils.isNoneEmpty(id)){
            UpgradeRecord record = upgradeRecordService.get(id);
            model.addAttribute("obj", record );

            List<AttMain> attMainList =attMainService.getAttMain(record.getUpgradeVersionId(), LauncherVersion.MODEL_NAME, "version");
            model.addAttribute("version",  launcherVersionService.get(record.getUpgradeVersionId()));
            model.addAttribute("attMain",attMainList.size()>0?attMainList.get(0):null);
        }
        return "sys/update/record/detail";
    }
}
