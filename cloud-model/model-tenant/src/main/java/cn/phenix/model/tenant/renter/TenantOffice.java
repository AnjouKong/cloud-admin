package cn.phenix.model.tenant.renter;

import cn.phenix.cloud.core.model.TreeEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * 机构Entity
 */
@Getter
@Setter
@Entity
@Table(name = "t_tenant_office")
@DynamicInsert
@DynamicUpdate
public class TenantOffice extends TreeEntity<TenantOffice> {

    private static final long serialVersionUID = 1L;

    private String code;    // 机构编码
    private String type;    // 机构类型（1：公司；2：部门；3：小组）
    private String grade;    // 机构等级（1：一级；2：二级；3：三级；4：四级）
    private String address; // 联系地址
    private String master;    // 负责人
    private String phone;    // 电话
    private String fax;    // 传真
    private String email;    // 邮箱
    private String useable;//是否可用
    private String tenantId;//商户id

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<TenantOffice> childDeptList;//快速添加子部门

    public TenantOffice() {
        super();
        this.type = "2";
    }

    public TenantOffice(String id) {
        super();
        this.id = id;
    }

    public String getParentId() {
        if(parent!=null)
            return parent.getId();
        return StringUtils.EMPTY;
    }

    public TenantOffice getParent() {
        return parent;
    }

    public void setParent(TenantOffice parent) {
        this.parent = parent;
    }


    @Override
    public String toString() {
        return name;
    }
}