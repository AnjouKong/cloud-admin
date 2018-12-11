package cn.phenix.model.log;


import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 设备异常日志
 * Created by xiaobin on 2017/8/24.
 */
@Getter
@Setter
@Entity
@Table(name = "LOG_DEVICE_ERROR")
public class DeviceErrorLog extends TenantBaseModel implements Serializable {

    //设备标识
    @Column
    private String deviceId;
    @Column
    private String roomId;

    //事件标识
    @Column
    private String eventCode;

    //事件描述
    @Column(length = 300)
    private String eventBody;

    //异常描述
    @Column
    private String errorDescribe;

    //终端版本
    @Column(length = 50)
//    @ColDefine(width = 50, type = ColType.VARCHAR)
    private String appVersion;


    //设备标识
    @Column(length = 50)
    private String groupCode;

    //房间号
    @Column(length = 150)
    private String groupName;

    @Column(length = 50)
    private String tenantId;

    @Column(length = 150)
    private String tenantName;


}
