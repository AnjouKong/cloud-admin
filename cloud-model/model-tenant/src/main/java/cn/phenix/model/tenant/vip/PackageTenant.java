package cn.phenix.model.tenant.vip;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author socilents
 * Create in 2018/5/24 11:36
 **/
@Getter
@Setter
@Entity
@Table(name="APP_PACKAGE_TENANT")
public class PackageTenant extends BaseModel {
    private String packageId;
    private String tenantId;
    private String tenancyName;
    private String packageName;
}
