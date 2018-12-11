package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Wizzer on 2016/7/30.
 */
@Getter
@Setter
@Table(name = "sys_task")
public class SysTask extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(length = 50)
    private String name;

    //执行类
    @Column(length = 255)
    private String jobClass;

    //任务说明
    @Column
    private String note;

    //定时规则
    @Column
    private String cron;

    //执行参数
    @Column
    private String data;

    //执行时间
    @Column
    private Integer exeAt;

    //执行结果
    @Column
    private String exeResult;

    //是否禁用
    @Column
    private boolean disabled;

}
