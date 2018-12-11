package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.TreeEntity;
import lombok.Getter;
import lombok.Setter;
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
@Table(name = "sys_office")
@DynamicInsert
@DynamicUpdate
public class SysOffice extends TreeEntity<SysOffice> {

    private static final long serialVersionUID = 1L;
    @Transient
    private SysArea area;        // 归属区域
    private String code;    // 机构编码
    //2018年2月26日14:38:28 机构类型来源于字典表，例如机构、部门、外部合作单位、终端厂商，存储字典表id；
    //字典表系统类型的id为jZ4dBJAj41P4iestmyK，用于树加载；
    private String type;
    private String grade;    // 机构等级（1：一级；2：二级；3：三级；4：四级）
    private String address; // 联系地址
    private String master;    // 负责人
    private String phone;    // 电话
    private String fax;    // 传真
    private String email;    // 邮箱
    private String useable;//是否可用

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<SysOffice> childDeptList;//快速添加子部门

    public SysOffice() {
        super();
        this.type = "2";
    }

    public SysOffice(String id) {
        super();
        this.id = id;
    }


    public SysOffice getParent() {
        return parent;
    }

    public void setParent(SysOffice parent) {
        this.parent = parent;
    }


    @Override
    public String toString() {
        return name;
    }
}