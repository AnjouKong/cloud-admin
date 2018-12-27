package cn.phenix.model.tenant.device;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;



/**
 * 设备用户
 * Created by xiaobin on 2017/8/14.
 */
@Getter
@Setter
@Entity
@Table(name="T_TENANT_DEVICE_USER")
@DynamicUpdate
@DynamicInsert
public class DeviceUser extends TenantBaseModel implements Serializable {


    private static final long serialVersionUID = -4037654712154776090L;

    //商户标识(兼容老系统)
    //新系统代表：设备组
    @Column
    private String deviceGroupCode;

    //设备系统
    @Column()
    private String deviceSystem;

    //设备Id
    @Column()
    private String deviceId;

    //设备厂商
    @Column
    private String deviceManufacturer;

    //设备型号
    @Column
    private String deviceModel;
    //设备Ip
    @Column
    private String deviceIp;
    //设备Mac
    @Column
    private String deviceMac;
    //设备版本号
    @Column
    private String versionId;

    //房间号
    @Column
    private String roomId;
    //对应的服务IP
    @Column
    private String serverIp;
    //对应的服务端口
    @Column
    private String serverPort;
    //对应省
    @Column
    private String province;
    @Column
    private String city;
    @Column
    private String cityCode;
    //定位地址(青岛市动漫产业园C座)
    @Column
    private String address;
    //经度
    @Column
    private String longitude;
    //维度
    @Column
    private String latitude;
    //商户Id
    @Column
    private String tenantId;
    //测试组id
    @Column
    private String testGroupId;


    //地址json格式
    @Column(length = 2000)
    private String addressJson;

    //扩展json(存储设备的信息，json格式)
    @Column(length = 2000)
    private String extensionJson;
    @Column
    private String domain;

    @Transient
    private String tenantName;
    @Transient
    private String groupName;
    @Transient
    private String roomIdOrDeviceId;

}
