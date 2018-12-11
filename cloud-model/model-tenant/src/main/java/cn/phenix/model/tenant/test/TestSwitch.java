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
 *  测试开关字典
 *
 **/
@Getter
@Setter
@Entity
@Table(name="T_TENANT_TEST_SWITCH")
public class TestSwitch extends BaseModel implements Serializable {


    private static final long serialVersionUID = 3185136047558175973L;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String code;

    @Transient
    private String value;
}
