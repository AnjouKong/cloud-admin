package cn.phenix.cloud.vo.log;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据统计VO
 * Created by mgm
 */
@Getter
@Setter
public class LogDeviceEventV1Vo extends BaseModel{

    private String id;
    private String deviceId;//设备id
    private String roomId;//房间号

    private String eventCode;//	事件标识
    private String eventBody;//事件名称

    private String startTime;//开始时间
    private String sstartTime;//开始时间 -start
    private String estartTime;//开始时间 -end


    private String endTime;//结束时间
    private String eventDescribe;//事件描述
    private String appVersion;//app版本
    private String tenantId;//商户id
    private String tenantName;//商户名称
    private String interfaceId;//接口id
    private String token;
    private String num;//统计数量
    private String resourceName;//影片名称
    private String createDay;//数据日期
    private String screateDay;//数据日期
    private String ecreateDay;//数据日期
}
