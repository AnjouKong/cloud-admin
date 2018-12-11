package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.TreeEntity;
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
 *  分类
 */
@Setter
@Getter
@Table(name = "sys_category")
@Entity
public class SysCategory extends TreeEntity<SysCategory> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 200)
    private String modelName;

    //名称
    @Column(length = 50)
    private String name;

    //排序字段
    @Column(length = 10)
    private Integer sort;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
    @OrderBy(value = "sort")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<SysCategory> categoryList = new ArrayList<>();

    @Override
    public SysCategory getParent() {
        return parent;
    }

    @Override
    public void setParent(SysCategory parent) {
        this.parent = parent;
    }
}
