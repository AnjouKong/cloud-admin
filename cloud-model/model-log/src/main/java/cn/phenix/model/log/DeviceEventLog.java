package cn.phenix.model.log;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 终端事件
 * Created by xiaobin on 2017/8/24.
 */
@Getter
@Setter
@Entity
@Table(name="LOG_DEVICE_EVENT")
public class DeviceEventLog extends TenantBaseModel implements Serializable {

    //接口Id
    @Column
    private String interfaceId;
    //设备标识
    @Column
    private String deviceId;

    @Column
    private String token;

    @Column
    private String tenantName;

    //房间号
    @Column
    private String roomId;

    //终端事件
    @Column
    private String eventCode;
    //终端事件描述

    @Column
    private String eventBody;

    //进入时间
    @Column
    private String startTime;
    //离开时间
    @Column
    private String endTime;

    //事件描述
    @Column
    private String eventDescribe;

    //终端版本
    @Column
    private String appVersion;

}
