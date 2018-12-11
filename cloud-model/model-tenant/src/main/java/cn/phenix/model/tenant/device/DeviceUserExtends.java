package cn.phenix.model.tenant.device;


import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备用户扩展表
 * Created by xiaobin on 2017/8/14.
 */
@Getter
@Setter
@Entity
@Table(name="T_DEVICE_USER_EXTENDS")
public class DeviceUserExtends extends TenantBaseModel {

    private static final long serialVersionUID = 8571366900492590889L;

    @Column
    private String hasPrivate;//是否含有私有节点

    @Column
    private String playIp;//私有节点播放地址

    @Column
    private String playPort;//私有节点播放端口
    @Column
    private String proxyIp;//数据代理地址

    @Column
    private String proxyPort;//数据代理端口

    @Column
    private String resourceProxyIp;//资源代理地址

    @Column
    private String resourceProxyPort;//资源代理端口

    @Column
    private String specificationId; //设备型号
}
