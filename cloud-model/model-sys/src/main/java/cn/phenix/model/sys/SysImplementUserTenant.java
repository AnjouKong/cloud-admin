package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 实施用户与商户关系
 */
@Setter
@Getter
@Table(name = "SYS_IMPLEMENT_USER_TENANT")
@Entity
@DynamicInsert
@DynamicUpdate
public class SysImplementUserTenant extends BaseModel {
    private String userId;
    private String tenantId;

}