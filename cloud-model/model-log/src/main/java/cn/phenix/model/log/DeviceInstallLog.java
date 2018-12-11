package cn.phenix.model.log;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备安装日志描述
 * Created by xiaobin on 2017/8/16.
 */
@Getter
@Setter
@Entity
@Table(name="LOG_DEVICE_INSTALL")
public class DeviceInstallLog extends BaseModel {

    private static final long serialVersionUID = -2915962437448821262L;
    //商户Id
    private String tenantId;
    //房间号
    private String roomNo;
    //设备id
    private String deviceId;

    private String deviceUserId;

    private String userId;

    private String province;

    private String city;

    private String cityCode;

    private String address;
    //经度
    private String longitude;
    //维度
    private String latitude;

    //地址json格式
    @Column(length = 2000)
    private String addressJson;
}
