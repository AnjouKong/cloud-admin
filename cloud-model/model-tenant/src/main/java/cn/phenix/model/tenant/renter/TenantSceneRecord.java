package cn.phenix.model.tenant.renter;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 商户场景记录
 */
@Getter
@Setter
@Entity
@Table(name = "t_tenant_scene_record")
public class TenantSceneRecord extends BaseModel {

    /**
     * 商户场景Id
     */
    @Column(length = 32)
    private String tenantSceneId;

    /**
     * 终端组Id
     */
    @Column(length = 32)
    private String groupId;

    /**
     * 版本
     */
    @Column(length = 50)
    private String version;


    @Transient
    private String sceneName;
}
