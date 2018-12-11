package cn.phenix.model.log;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 私有节点心跳服务
 *
 * @author xiaobin
 * @create 2018-02-02 下午1:28
 **/
@Getter
@Setter
@Entity
@Table(name = "LOG_PRIVATE_NODE")
public class PrivateNodeLog extends TenantBaseModel implements Serializable {

    //上次获取的IP
    private String lastIP;

    //当前系统的IP
    private String thisIP;

    //节点事件
    @Column
    private String eventCode;
    //节点描述
    @Column(length = 2500)
    private String eventBody;

}
