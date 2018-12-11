package cn.phenix.model.tenant.test;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 测试组
 **/
@Getter
@Setter
@Entity
@Table(name = "T_TENANT_TEST_GROUP_SWITCH_CONFIG")
public class TestGroupSwitchConfig extends BaseModel implements Serializable {


    private static final long serialVersionUID = -4608600308060452313L;
    @Column(length = 100)
    private String groupId;

    @Column(length = 100)
    private String switchId;

    @Column(length = 10)
    private String switchValue;

    @Transient
    private String switchName;

}
