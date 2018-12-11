package cn.phenix.model.log;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by xiaobin on 2017/8/17.
 */
@Getter
@Setter
public class DeviceInfo implements Serializable {


    private static final long serialVersionUID = -7280494040536173085L;

    //商户标识(兼容老系统)---新系统里是groupCode
    private String tenancyCode;

    //组标识
    private String groupCode;

    //组名称
    private String groupName;

    //租户托管状态
    private String tenancyType;
    //租户Id
    private String tenantId;

    private String tenantName;

    //设备主键
    private String deviceUserId;

    private String deviceId;
    //设备版本号

    private String versionId;

    private String roomId;

    private String serverIp;

    private String province;

    private String city;
    private String cityCode;
    //定位地址(青岛市动漫产业园C座)
    private String address;
    //经度
    private String longitude;
    //维度
    private String latitude;
    private String deviceIp;


    public DeviceInfo() {
    }

}
