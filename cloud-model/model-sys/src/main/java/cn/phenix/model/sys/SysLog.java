package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
@Entity
@Table(name = "sys_log")
@Getter
@Setter
public class SysLog extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    //操作者
    @Column(length = 100)
    private String username;

    //请求类型 get/post
    @Column
    private String type;

    //api注释中的tag
    @Column(length = 50)
    private String tag;

    //执行类
    @Column
    private String method;

    //来源IP
    @Column
    private String ip;

    //请求url
    @Column
    private String url;

    //请求参数
    @Column(columnDefinition = "TEXT")
    private String args;

}
