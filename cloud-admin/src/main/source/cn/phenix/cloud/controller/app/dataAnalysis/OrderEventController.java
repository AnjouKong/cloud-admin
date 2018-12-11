package cn.phenix.cloud.controller.app.dataAnalysis;


import cn.phenix.cloud.admin.tenant.renter.service.TenancyService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.log.service.OrderEventService;
import cn.phenix.cloud.vo.log.LogDeviceEventV1Vo;
import cn.phenix.event.EventEnum;
import cn.phenix.model.tenant.renter.Tenancy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("platform/dataAnalysis/order")
@Api(tags = "付费订单分析")
@RequiresPermissions("sys.manager.unit")
public class OrderEventController extends BaseController {

    @Autowired
    private OrderEventService orderEventService;

    @Autowired
    private TenancyService tenancyService;


    @RequestMapping("")
    @ApiOperation(value = "付费订单分析首页")
    public String index(Model model) {
        Map<String,String> eventMap=new HashMap<>();
        for (EventEnum eventEnum : EventEnum.values()) {
            eventMap.put(eventEnum.getInfo(),eventEnum.name());
        }
        List<Tenancy> tenancyList = tenancyService.findByStatus("1");
        model.addAttribute("eventMap",eventMap);
        model.addAttribute("tenancyList",tenancyList);
        return "app/dataAnalysis/order/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "付费订单分析首页数据加载")
    public PageTable<LogDeviceEventV1Vo> data(Integer draw, DataTableParameter parameter, LogDeviceEventV1Vo eventV1Vo) {
        PageRequest pageRequest = parameter.getPageRequest();

        PageTable<LogDeviceEventV1Vo> pageTable = orderEventService.findPage(pageRequest,eventV1Vo);
        pageTable.setDraw(draw);
        return pageTable;
    }
}
