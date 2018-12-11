package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Wizzer on 2016/12/6.
 */
@Getter
@Setter
@Entity
@Table(name = "sys_plugin")
//@TableIndexes({@Index(name = "INDEX_SYS_PLUGIN", fields = {"code"}, unique = true),@Index(name = "INDEX_SYS_CLASSNAME", fields = {"className"}, unique = true)})
public class SysPlugin extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    //唯一标识
    @Column
    private String code;

    //类名
    @Column
    private String className;

    //执行参数
    @Column
    private String args;

    //文件路径
    @Column
    private String path;

    //是否禁用
    @Column
    private boolean disabled;
}
