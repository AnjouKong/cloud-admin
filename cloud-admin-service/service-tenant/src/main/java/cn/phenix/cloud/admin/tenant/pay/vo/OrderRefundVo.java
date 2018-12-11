package cn.phenix.cloud.admin.tenant.pay.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 订单退款相关
 *
 * @author xiaobin
 * @create 2017-08-30 上午11:34
 **/
@Getter
@Setter
@ToString
@ApiModel("订单退款相关")
public class OrderRefundVo {

    @ApiModelProperty("系统授权的服务标识")
    private String appId;

    @ApiModelProperty("时间戳")
    private String timestamp;

    @ApiModelProperty("支付平台订单号")
    private String tradeNo;

    @ApiModelProperty("商户单号")
    private String orderNo;

    @ApiModelProperty("退款金额(保留後邊兩位)")
    private String refundAmount;

    @ApiModelProperty("总金额(保留後邊兩位)")
    private String totalAmount;

    @ApiModelProperty("认证码")
    private String signature;

}
