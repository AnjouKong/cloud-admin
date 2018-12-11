package cn.phenix.model.tenant.device;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 设备组
 *
 * @author xiaobin
 * @create 2017-08-29 下午12:11
 **/
@Getter
@Setter
@Entity
@Table(name="T_TENANT_DEVICE_USER_GROUP")
public class DeviceUserGroup extends TenantBaseModel implements Serializable {

    private static final long serialVersionUID = 3362158825658433723L;

    @Column
    private String groupName;
    /**
     * 模板id
     */
    @Column
    private String sceneId;

    @Transient
    private String tenantName;

    @Transient
    private String sceneName;
}
