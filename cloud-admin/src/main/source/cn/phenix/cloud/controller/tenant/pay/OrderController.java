package cn.phenix.cloud.controller.tenant.pay;


import cn.phenix.cloud.admin.sys.service.SysApiService;
import cn.phenix.cloud.admin.tenant.pay.config.PayConf;
import cn.phenix.cloud.admin.tenant.pay.service.MallOrderEventService;
import cn.phenix.cloud.admin.tenant.pay.service.OrderService;
import cn.phenix.cloud.admin.tenant.pay.vo.OrderRefundVo;
import cn.phenix.cloud.admin.tenant.pay.vo.ResponseJson;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.JsonUtils;
import cn.phenix.cloud.core.utils.MD5Util;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.mall.MallOrder;
import cn.phenix.model.sys.SysApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


/**
 * 应用管理
 **/
@Controller
@Api(tags = "订单管理")
@RequestMapping("platform/operate/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SysApiService sysApiService;

    @Autowired
    private PayConf payConf;

    @Autowired
    private MallOrderEventService mallOrderEventService;


    @GetMapping("")
    @ApiOperation("首页跳转")
    @RequiresPermissions("sys.manager.unit")
    public String index() {
        return "tenant/pay/index";
    }


    @PostMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    @RequiresPermissions("sys.manager.unit")
    public PageTable<MallOrder> data(Integer draw,DataTableParameter parameter, MallOrder order) {

        PageTable<MallOrder> pageTable = orderService.findPage(parameter, order);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("detail/{id}")
    @ApiOperation("查看详情")
    @RequiresPermissions("sys.manager.unit")
    public String detail(@PathVariable("id") String id, String orderNo,Model model) {
        model.addAttribute("obj", orderService.get(id));
        model.addAttribute("orderEvent", mallOrderEventService.findByOrderNoOrderByCreateDateDesc(orderNo));
        return "tenant/pay/detail";
    }

    /**
     * 退款
     *
     * @param id 订单id
     * @return
     */
    @RequestMapping(value = "refund/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("退款")
    public Result refund(@PathVariable("id") String id) {
        //封装参数
        OrderRefundVo refundVo = new OrderRefundVo();
        this.packageOrderRefundVo(id, refundVo);
        //创建一个头部对象
        HttpHeaders headers = new HttpHeaders();
        //设置contentType
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        //设置我们的请求信息，第一个参数为请求Body,第二个参数为请求头信息
        HttpEntity<String> strEntity = new HttpEntity(JsonUtils.writeObjectToJson(refundVo), headers);
        RestTemplate restTemplate = new RestTemplate();
        //请求接口
        String restJson = restTemplate.postForObject(payConf.getRefundUrl(), strEntity, String.class, refundVo);
        ResponseJson json = JsonUtils.readObjectByJson(restJson, ResponseJson.class);
        if (json.getHead().getCode() == 200) {   // 退款成功，修改订单状态
            orderService.updateRefundStatusById(id);
            return Result.success(json.getHead().getMsg());
        }else{
            return Result.error(json.getHead().getMsg());
        }
    }

    /**
     * 封装订单退款vo类
     *
     * @param id       订单id
     * @param refundVo vo类
     */
    private void packageOrderRefundVo(String id, OrderRefundVo refundVo) {
        MallOrder order = orderService.get(id);  //获取订单信息
        SysApi api = sysApiService.findByDelFlagAndAppId("b79d5cb8c3"); //获取终端
        refundVo.setAppId(api.getAppId());
        refundVo.setOrderNo(order.getOrderNo());
        refundVo.setRefundAmount(order.getPrice() + "");
        refundVo.setTimestamp(System.currentTimeMillis() + "");
        refundVo.setTotalAmount(order.getPrice() + "");
        refundVo.setRefundAmount(order.getPrice()+"");
        refundVo.setTimestamp(System.currentTimeMillis()+"");
        refundVo.setTotalAmount(order.getPrice()+"");
        refundVo.setTradeNo(order.getTradeNo());
        StringBuffer sb = new StringBuffer();
        sb.append(refundVo.getAppId()).append(refundVo.getTimestamp()).append(refundVo.getTradeNo()).append(refundVo.getOrderNo()).append(refundVo.getRefundAmount()).append(refundVo.getTotalAmount()).append("#").append(api.getAppSecret()); //混淆码
        refundVo.setSignature(MD5Util.getMD5String(sb.toString()));
    }

}