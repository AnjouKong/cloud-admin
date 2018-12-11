package cn.phenix.model.mall;

import cn.phenix.cloud.core.utils.DateUtils;
import cn.phenix.model.tenant.base.TenantBaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单
 *
 * @author xiaobin
 * @create 2017-11-01 下午12:17
 **/
@Getter
@Setter
@Entity
@Table(name = "MALL_ORDER")
public class MallOrder extends TenantBaseModel {

    //#
    // 商户
    @Column
    private String tenantName;

    //#
    //房間號
    @Column
    private String roomId;

    //设备id
    @Column
    private String deviceId;

    //支付用户(C端用户)
    @Column
    private String userId;

    /**
     * 收货人姓名
     */
    @Column
    private String acceptName;

    /**
     * 收货人电话
     */
    @Column
    private String telPhone;

    //会话token
    @Column
    private String token;

    //#
    // 订单标题
    @Column
    private String subject;

    //订单描述
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

    //币种
    @Column
    private String curType;

    //#
    //订单编号()
    @Column(name = "orderNo")
    private String orderNo;

    //退款单号
    private String refundNo;

    //#
    //支付方式(aliPay：支付宝，wxPay：微信)
    @Column
    private String payType;

    //支付類型(如果是微信，可选参数：1、JSAPI（公众号支付）；2：NATIVE（二维码）；3：APP（移动支付）；）
    // （如果是支付宝，可选参数：1、SWEEPPAY（二维码支付）；2：BAR_CODE（条码付）)
    @Column
    private String transactionType;


    //订单总额
    @Column(precision = 9,scale = 2)
    private BigDecimal price;

    //实付总额
    @Column(precision = 9,scale = 2)
    private BigDecimal realAmount;

    //运费金额
    @Column(precision = 9,scale = 2)
    private BigDecimal payableFreight;

    //实付运费
    @Column(precision = 9,scale = 2)
    private BigDecimal realFreight;

    //#
    // 支付平台的订单号（交易单号）
    @Column
    private String tradeNo;

    //购买时间
    @Column
    private Date payTime;

    //支付回调通知时间
    @Column
    private String notifyTime;

    //购买人（阿里存在，微信不存在）
    @Column
    private String buyerEr;
    //回调后返回的详细信息
    @Column
    private String backJson;

    @Column
    private String appVersion;


    //订单状态 1生成订单,2支付订单,3取消订单(客户触发),4作废订单(管理员触发),5完成订单,6退款(订单完成后),7部分退款(订单完成后)
    @Column
    private String status;

    //#
    //支付状态 Unpaid：未付款、Paid：已付款、Shipped：已发货、3：确认收货、4：售后、error：付款异常（异常记录存储到remark）;Refund：退款
    @Column
    private String payStatus;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<MallOrderGoods> orderGoodsList = Lists.newArrayList();
    @Transient
    public String getCreateDateStr(){
        return DateUtils.format(this.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
    }

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
