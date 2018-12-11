package cn.phenix.cloud.controller.tenant.pms;//package cn.phenix.cloud.controller.app.order;


import cn.phenix.cloud.admin.tenant.pms.service.CheckInService;
import cn.phenix.cloud.admin.tenant.pms.service.GuestInfoService;
import cn.phenix.cloud.admin.tenant.pms.vo.GuestInfoVo;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * pms管理
 *
 **/
@Controller
@Api(tags = "pms管理")
@RequestMapping("platform/operate/guest")
public class GuestInfoController extends BaseController{

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private GuestInfoService guestInfoService;



    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "tenant/pms/index";
    }


    @PostMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    @RequiresPermissions("sys.manager.unit")
    public PageTable<GuestInfoVo> data(DataTableParameter parameter, GuestInfoVo guestInfoVo) {
        return guestInfoService.findPage(parameter,guestInfoVo);
    }

}