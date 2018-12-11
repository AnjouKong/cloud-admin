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
 * Created by wizzer on 2016/6/21.
 */
@Table(name = "sys_dict")
@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
public class SysDict extends TreeEntity<SysDict> implements Serializable {
    private static final long serialVersionUID = 1L;


    //名称
    @Column
    private String name;

    //代码
    @Column(length = 200)
    private String code;

    //排序字段
    @Column
    private Integer sort;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
    @OrderBy(value = "sort")
    @NotFound(action = NotFoundAction.IGNORE)
    private List<SysDict> dictList = new ArrayList<>();

    @Override
    public SysDict getParent() {
        return parent;
    }

    @Override
    public void setParent(SysDict parent) {
        this.parent = parent;
    }
}
