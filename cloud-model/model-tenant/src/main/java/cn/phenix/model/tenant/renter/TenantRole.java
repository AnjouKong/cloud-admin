package cn.phenix.model.tenant.renter;

import cn.phenix.cloud.core.config.Global;
import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.cloud.core.model.BaseRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色Entity
 *
 * @author ThinkGem
 * @version 2013-12-05
 */
@Getter
@Setter
@Entity
@Table(name = "t_tenant_role")
@DynamicInsert
@DynamicUpdate
public class TenantRole extends BaseModel implements BaseRole {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officeId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private TenantOffice office;
    private String name;    // 角色名称
    private String enname;    // 英文名称(角色编号)
    private String roleType;// 权限类型
    private String dataScope;// 数据范围

    private String sysData;        //是否是系统数据
    private String useable;        //是否是可用
    private String tenantId;        //是否是可用


    public TenantRole() {
        super();
        this.dataScope = DATA_SCOPE_SELF;
        this.useable = Global.YES;
    }

}
