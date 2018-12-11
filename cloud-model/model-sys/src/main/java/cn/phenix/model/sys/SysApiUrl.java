package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 接口链接
 *
 * @author xiaobin
 * @create 2017-10-13 下午5:11
 **/
@Table(name = "sys_api_url")
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class SysApiUrl extends BaseModel implements Serializable {

    //链接名称
    private String name;

    //链接地址
    private String url;

    //是否需要认证
    private Boolean needAuth;

    //是否有效
    private Boolean enable;
}
