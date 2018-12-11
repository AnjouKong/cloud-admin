package cn.phenix.model.sys;

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
@Table(name = "sys_role")
@DynamicInsert
@DynamicUpdate
public class SysRole extends BaseModel implements BaseRole {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officeId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private SysOffice office;
    private String name;    // 角色名称
    private String enname;    // 英文名称(角色编号)
    private String roleType;// 权限类型
    private String dataScope;// 数据范围

    private String sysData;        //是否是系统数据
    private String useable;        //是否是可用

    //@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    //@JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "userId")})


    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "sys_role_menu", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "menuId")})
    @JsonIgnore
    private List<SysMenu> menuList = new ArrayList<>(); // 拥有菜单列表


    public SysRole() {
        super();
        this.dataScope = DATA_SCOPE_SELF;
        this.useable = Global.YES;
    }

    public List<String> getMenuIdList() {
        List<String> menuIdList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        return menuIdList;
    }

    public void setMenuIdList(List<String> menuIdList) {
        menuList = new ArrayList<>();
        for (String menuId : menuIdList) {
            SysMenu menu = new SysMenu();
            menu.setId(menuId);
            menuList.add(menu);
        }
    }

    public String getMenuIds() {
        return StringUtils.join(getMenuIdList(), ",");
    }

    public void setMenuIds(String menuIds) {
        menuList = new ArrayList<>();
        if (menuIds != null) {
            String[] ids = StringUtils.split(menuIds, ",");
            setMenuIdList(Arrays.asList(ids));
        }
    }

    /**
     * 获取权限字符串列表
     */
    public List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (menu.getPermission() != null && !"".equals(menu.getPermission())) {
                permissions.add(menu.getPermission());
            }
        }
        return permissions;
    }
}
