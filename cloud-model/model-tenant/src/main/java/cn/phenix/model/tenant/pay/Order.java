package cn.phenix.model.tenant.pay;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付订单
 * Created by xiaobin on 2017/8/18.
 */
@Getter
@Setter
@Entity
@Table(name="T_TENANT_PAY_ORDER")
public class Order extends TenantBaseModel implements Serializable {

    private static final long serialVersionUID = -5825191339606237554L;

    public static final String MODEL_NAME = "order";

    //#
    // 商户
    @Column
    private String tenantName;

    //会话token
    @Column
    private String token;

    //#
    // 商品名称
    @Column
    private String subject;

    //商品描述
    @Column
    private String body;

    //银行卡类型
    @Column
    private String bankType;

    //设备号
    @Column
    private String deviceInfo;

    //付款条码串  与设备号类似？？？
    @Column
    private String authCode;
    //用于公众号支付
    @Column
    private String openid;

    //币种（冲出中国，走向世界）
    @Column
    private String curType;

    //#
    //订单编号()
    @Column(name="orderNo")
    private String orderNo;

    //#
    //支付方式(aliPay：支付宝，wxPay：微信)
    @Column
    private String payType;

    //支付類型(如果是微信，可选参数：1、JSAPI（公众号支付）；2：NATIVE（二维码）；3：APP（移动支付）；）
    // （如果是支付宝，可选参数：1、SWEEPPAY（二维码支付）；2：BAR_CODE（条码付）)
    @Column
    private String transactionType;

    //支付用户(C端用户)
    @Column
    private String userId;
    //设备Id
    @Column
    private String deviceId;

    //#
    //房間號
    @Column
    private String roomId;

    //媒资集Id
    @Column
    private String goodId;
    //商品类型
    @Column
    private String goodType;

    //#
    //支付状态 Unpaid：未付款、Paid：已付款、Shipped：已发货、3：确认收货、4：售后、error：付款异常（异常记录存储到remark）
    @Column
    private String payStatus;
    //#
    //订单价格
    @Column(precision = 2)
//    @ColDefine(precision = 2)
    private BigDecimal price;

    //#
    // 支付平台的订单号（交易单号）
    @Column
    private String tradeNo;

    //支付回调通知时间
    @Column
    private String notifyTime;
    //购买人（阿里存在，微信不存在）

    //#
    @Column
    private String buyerEr;
    //回调后返回的详细信息
    @Column
    private String backJson;

    @Column
    private String appVersion;

    //退款状态   0 / null 未退款  1 已退款
    @Column
    private String refundStatus;


    /**
     * 未付款
     */
    public static final String Unpaid = "Unpaid";

    /**
     * 已付款
     */
    public static final String Paid = "Paid";

    /**
     * 已发货
     */
    public static final String Shipped = "Shipped";

    /**
     * 异常
     */
    public static final String Error = "error";

}
