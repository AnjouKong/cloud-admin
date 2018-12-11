package cn.phenix.cloud.controller.app.dataAnalysis;


import cn.phenix.cloud.admin.tenant.renter.service.TenancyService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.log.service.SummaryEventService;
import cn.phenix.cloud.vo.log.SummaryEventVo;
import cn.phenix.model.tenant.renter.Tenancy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("platform/dataAnalysis/summary")
@Api(tags = "分酒店统计汇总")
@RequiresPermissions("sys.manager.unit")
public class SummaryEventController extends BaseController {

    @Autowired
    private SummaryEventService sumaryEventService;
    @Autowired
    private TenancyService tenancyService;

    @RequestMapping("")
    @ApiOperation(value = "分酒店统计汇总首页")
    public String index(Model model) {
        List<Tenancy> tenancyList = tenancyService.findByStatus("1");
        model.addAttribute("tenancyList",tenancyList);
        return "app/dataAnalysis/summary/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "分酒店统计汇总数据加载")
    public PageTable<SummaryEventVo> data(Integer draw, DataTableParameter parameter, SummaryEventVo summaryEventVo) {
        PageRequest pageRequest = parameter.getPageRequest();

        PageTable<SummaryEventVo> pageTable = sumaryEventService.findPage(pageRequest,summaryEventVo);
        pageTable.setDraw(draw);
        return pageTable;
    }
    @GetMapping("/detail/{id}")
    @ApiOperation("详情")
    @RequiresPermissions("sys.manager.unit")
    public Object detail(@PathVariable("id")  String id, Model model) {
        if (!Strings.isBlank(id)) {
            SummaryEventVo summaryEventVo = sumaryEventService.findById(id);
            model.addAttribute("obj",summaryEventVo);
            return "app/dataAnalysis/summary/detail";
        }
        return null;
    }
}
