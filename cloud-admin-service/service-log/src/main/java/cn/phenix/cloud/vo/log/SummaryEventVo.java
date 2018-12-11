package cn.phenix.cloud.vo.log;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 首页概览VO
 * Created by mgm
 */
@Getter
@Setter
public class SummaryEventVo extends BaseModel{

    private String id;
    private String loginNum;//开机数
    private String orderNum;//订单数

    private String playNum;//	点播数
    private String playDeviceNum;//点播设备数

    private String roomNum;//房间数
    private String deviceNum;//设备数
    private String tenantId;//商户id

    private String tenantName;//商户名称
    private String createDay;//数据日期
    private String screateDay;//数据开始日期
    private String ecreateDay;//数据结束日期
    private String paidNum;//付费订单总数
    private String unpaidNum;//未付款订单总数
    private String shippedNum;//已发货订单总数
    private String sumPrice;//付费总额

}
