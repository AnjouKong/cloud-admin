package cn.phenix.cloud.controller.tenant.pay;


import cn.phenix.cloud.admin.tenant.pay.service.MallOrderGoodsService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.mall.MallOrderGoods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理
 **/
@Controller
@Api(tags = "订单详情管理")
@RequestMapping("platform/operate/order/goods")
public class MallOrderGoodsController extends BaseController {

    @Autowired
    private MallOrderGoodsService mallOrderGoodsService;

    @GetMapping("")
    @ApiOperation("首页跳转")
    @RequiresPermissions("sys.manager.unit")
    public String index(String orderId,Model model) {
        model.addAttribute("orderId",orderId);
        return "tenant/pay/goods";
    }


    @PostMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    @RequiresPermissions("sys.manager.unit")
    public PageTable<MallOrderGoods> data(Integer draw,DataTableParameter parameter,String orderId) {

        PageTable<MallOrderGoods> pageTable = mallOrderGoodsService.findPage(parameter, orderId);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("detail/{id}")
    @ApiOperation("查看详情")
    @RequiresPermissions("sys.manager.unit")
    public String detail(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", mallOrderGoodsService.get(id));
        return "tenant/pay/goodsDetail";
    }

}