package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 网关设置
 * @author xiaobin
 * @create 2017-09-06 下午1:00
 **/
@Table(name = "sys_get_way")
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class SysGetWay extends BaseModel implements Serializable {

    //网关编号
    @Column
    private String getWayCode;

    //网关服务地址
    @Column
    private String getWayPath;

    //网关服务id
    @Column
    private String serviceId;

    //启用正则匹配（默认为true）
    @Column
    private Boolean stripPrefix = true;

    //是否失败重处理
    @Column
    private Boolean retryable;

    //需要转发的url（有两个要素：1：没有定义serviceId；2：zuul 脱离服务注册）
    @Column
    private String toUrl;

    //接口名称
    @Column
    private String apiName;

    //是否启用 0是1否
    @Column
    private String enabled;
}
