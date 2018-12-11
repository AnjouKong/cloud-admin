package cn.phenix.model.tenant.renter;

import cn.phenix.cloud.core.model.BaseUser;
import cn.phenix.cloud.core.utils.CollectionUtil;
import cn.phenix.model.tenant.base.TenantBaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 租户用户基本信息
 * Created by zhadaojian on 2017/8/15.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="t_tenant_user")
public class TenantUser extends TenantBaseModel implements BaseUser {
   private String address;
   private String email;
   private String loginName;
   private String password;
   private String telephone;
   private String tenantId;
   private String tenantName;
   private String userName;
   private boolean isAdmin;

   public TenantUser() {
   }

   public TenantUser(String id) {
      this.id = id;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "companyId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
   @JsonIgnore
   private TenantOffice company;    // 归属公司
   @ManyToOne()
   @JoinColumn(name = "officeId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
   @JsonIgnore
   private TenantOffice office;    // 归属部门

   @Transient
   private TenantRole role;    // 根据角色查询用户条件

   @JsonIgnore
   @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
   @Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
   @JoinTable(name = "t_tenant_user_role",
           joinColumns = {@JoinColumn(name = "userId")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
   private List<TenantRole> roleList = new ArrayList<>(); // 拥有角色列表

   @JsonIgnore
   @Transient
   public List<String> getRoleIdList() {
      List<String> roleIdList = new ArrayList<>();
      for (TenantRole role : roleList) {
         roleIdList.add(role.getId());
      }
      return roleIdList;
   }

   public void setRoleIdList(List<String> roleIdList) {
      roleList = new ArrayList<>();
      for (String roleId : roleIdList) {
         TenantRole role = new TenantRole();
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

   @Override
   @Transient
   public String getName() {
      return userName;
   }

   @Override
   public boolean isAdmin() {
      return this.isAdmin;
   }


   @Override
   @Transient
   public List<String> getRoleCodeList() {
      List<String> roleCodeList = new ArrayList<>();
      for (TenantRole role : roleList) {
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

   @Override
   public String getLoginFlag() {
      return null;
   }
}
