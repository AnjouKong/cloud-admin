package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.cloud.core.model.BaseUser;
import cn.phenix.cloud.core.utils.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wizzer on 2016/6/21.
 */
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseModel implements BaseUser, Serializable {
    private static final long serialVersionUID = 1L;

    public SysUser() {
    }

    public SysUser(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private SysOffice company;    // 归属公司
    @ManyToOne()
    @JoinColumn(name = "officeId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private SysOffice office;    // 归属部门
    private String loginName;// 登录名
    private String password;// 密码
    private String no;        // 工号
    private String name;    // 姓名
    private String email;    // 邮箱
    private String phone;    // 电话
    private String mobile;    // 手机
    private String userType;// 用户类型
    private String loginIp;    // 最后登陆IP
    private Date loginDate;    // 最后登陆日期
    private String loginFlag;    // 是否允许登陆
    private String photo;    // 头像

    @Transient
    private SysRole role;    // 根据角色查询用户条件

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "userId")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<SysRole> roleList = new ArrayList<>(); // 拥有角色列表

    @JsonIgnore
    @Transient
    public List<String> getRoleIdList() {
        List<String> roleIdList = new ArrayList<>();
        for (SysRole role : roleList) {
            roleIdList.add(role.getId());
        }
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        roleList = new ArrayList<>();
        for (String roleId : roleIdList) {
            SysRole role = new SysRole();
            role.setId(roleId);
            roleList.add(role);
        }
    }

    /**
     * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
     */
    @Transient
    public String getRoleNames() {
        return CollectionUtil.extractToString(roleList, "name", ",");
    }

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(String id) {
        return id != null && "1".equals(id);
    }

    @Override
    @Transient
    public List<String> getRoleCodeList() {
        List<String> roleCodeList = new ArrayList<>();
        for (SysRole role : roleList) {
            roleCodeList.add(role.getEnname());
        }
        return roleCodeList;
    }

    @Override
    @Transient
    public String getCompanyId() {
        return company.getId();
    }

    @Override
    @Transient
    public String getOfficeId() {
        return office == null ? null : office.getId();
    }

    @Override
    public String getCompanyParentIds() {
        return company.getParentIds();
    }

    @Override
    public String getOfficeParentIds() {
        return office == null ? null : office.getParentIds();
    }
}
