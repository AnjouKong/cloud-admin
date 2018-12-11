package cn.phenix.model.tenant.test;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 *  测试组
 *
 **/
@Getter
@Setter
@Entity
@Table(name="T_TENANT_TEST_GROUP")
public class TestGroup extends TenantBaseModel implements Serializable {

    private static final long serialVersionUID = 3362158825658433724L;

    @Column(length = 100)
    private String groupName;

    @Transient
    private String tenantName;

    @Transient
    private String switchConfig;

}
