package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.TreeEntity;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/21.
 */
@Getter
@Setter
@Table(name = "sys_menu")
@Entity
@DynamicInsert
@DynamicUpdate
public class SysMenu extends TreeEntity<SysMenu> implements Serializable {
    private static final long serialVersionUID = 1L;

    public SysMenu() {
        super();
    }

    @Override
    public SysMenu getParent() {
        return parent;
    }

    @Override
    public void setParent(SysMenu parent) {
        this.parent = parent;
    }

    public SysMenu(String id) {
        super();
        this.id = id;
    }

    public SysMenu(String id, Integer sort) {
        super();
        this.id = id;
        this.sort = sort;
    }

    private String parentIds; // 所有父级编号

    //树路径
    @Column(length = 100)
    private String path;

    //菜单名称
    @Column(length = 100)
    private String name;

    //菜单别名
    @Column(length = 100)
    private String aliasName;

    //资源类型
    @Column(length = 10)
    private String type;

    //菜单链接
    @Column
    private String href;

    //打开方式
    @Column(length = 50)
    private String target;

    //菜单图标
    @Column(length = 50)
    private String icon;

    //是否显示
    @Column
    private Boolean isShow;

    //权限标识
    @Column
    private String permission;

    //排序字段
    @Column
    /*@Comment("排序字段")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM sys_menu"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM sys_menu")
    })*/
    private Integer sort;

    //有子节点
    @Transient
    private Boolean hasChildren;

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @Transient
    private String userId;

    //@ManyToOne
    //private SysMenu parent;	// 父级菜单

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
    @OrderBy(value = "sort")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<SysMenu> menuList = new ArrayList<>();

    @ManyToMany(mappedBy = "menuList", fetch = FetchType.LAZY)
    @Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
    private List<SysRole> roleList = Lists.newArrayList(); // 拥有角色列表
}
