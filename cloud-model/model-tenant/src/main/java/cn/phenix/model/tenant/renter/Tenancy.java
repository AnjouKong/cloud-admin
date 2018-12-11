package cn.phenix.model.tenant.renter;

import cn.phenix.cloud.core.utils.DateUtils;
import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.sql.Update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 租户基本描述
 * Created by xiaobin on 2017/8/12.
 */
@Getter
@Setter
@Entity
//@DynamicUpdate
//@DynamicInsert
@Table(name = "T_TENANT")
public class Tenancy extends TenantBaseModel implements Serializable {

    private static final long serialVersionUID = -6996938485925972040L;

    public Tenancy() {
        super();
        this.setTenantId(this.getId());
    }

    public Tenancy(String id) {
        super();
        this.id = id;
        this.setTenantId(id);
    }

    //租户编号
    @Column
    private String tenancyCode;
    //所属者ID
    @Column
    private String sysUserId;
    //所属者名称
    @Column
    private String sysUserName;
    //邮箱
    @Column
    private String email;
    //联系电话
    @Column
    private String telephone;
    //租户名称（归属者）
    @Column
    private String tenancyName;
    //租户类型(0:托管；1：私有化)
    @Column
    private String tenancyType;
    //租户状态(0:待审核；1：通过)
    @Column
    private String status;

    //租户级别  废弃
    @Column
    private String levelId;

    /**
     * 策略Id 新添
     */
    @Column(length = 100)
    private String strategyId;

    //租户所属地区
    @Column
    private String areaId;
    //租户所属地区全称
    @Column
    private String areaName;
    @Column(length = 10)
    private String hasPrivate;
    @Column(length = 20)
    private String ip;
    @Column(length = 10)
    private String playPort;
    @Column(length = 10)
    private String proxyPort;
    @Column(length = 10)
    private String resourceProxyPort;
    @Column
    private Integer deviceNum;
    @Column(columnDefinition = "TINYINT", length = 1)
    private Boolean isImplement;
    @Transient
    private String[] languageDic;

    @Transient
    public String getCreateDateStr() {
        return DateUtils.format(this.getCreateDate(), "yyyy-MM-dd HH:mm:ss");
    }

}
