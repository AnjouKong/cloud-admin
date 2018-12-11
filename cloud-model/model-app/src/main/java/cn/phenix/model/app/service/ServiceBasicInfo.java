package cn.phenix.model.app.service;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 服务基本信息
 */

@Getter
@Setter
@Entity
@Table(name = "app_service_basic_info")
public class ServiceBasicInfo extends BaseModel {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务地址
     */
    private String serviceUrl;

}
