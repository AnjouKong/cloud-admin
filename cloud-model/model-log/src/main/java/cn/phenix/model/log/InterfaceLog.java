package cn.phenix.model.log;

import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 接口异常日志
 *
 * @author xiaobin
 * @create 2017-08-27 上午11:42
 **/
@Getter
@Setter
@Entity
@Table(name="LOG_INTERFACE_INFO")
public class InterfaceLog extends TenantBaseModel implements Serializable {

    @Column
    private String controllerName;

    @Column
    private String methodName;

    @Column
    private String token;

    @Column
    private String controllerInfo;

    @Column
    private String methodInfo;

    //请求方式
    @Column
    private String requestMethod;
    //接口参数
    @Column
//    @ColDefine(type = ColType.TEXT)
    private String interfaceParamsJson;


    @Column
    private String tenantName;

    @Column
    private String deviceId;

    @Column
    private String roomId;

    @Column
    private String appVersion;

    //接口版本
    @Column
    private String interfaceVersion;

    //返回信息
    @Column
    private String returnJson;

    //1：出现错误
    @Column
    private String isError;

    @Column
    private String errorInfo;

    //进入时间
    @Column
    private String startTime;
    //离开时间
    @Column
    private String endTime;

    //时长(毫秒)
    @Column
    private Long whenLong;


}
