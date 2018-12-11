package cn.phenix.model.mall;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author xiaobin
 * @create 2018-03-12 下午4:36
 **/
@Getter
@Setter
@Entity
@Table(name = "MALL_ORDER_EVENT")
public class MallOrderEvent extends TenantBaseModel {

    private String apiVersion;

    //订单编号
    private String orderNo;

    /**
     * event code
     */
    //launchOrder ： 生成本地订单
    //wxAliQr: 生成二维码
    //getOpenId:获取openId（微信）
    //getCode:openId换取用户code（微信）
    //qrPay:支付第三方订单
    //notifyOrder:第三方通知
    //paid:支付成功
    //shipped:和终端完成对接
    private String eventCode;

    //事件说明
    private String eventBody;

}
