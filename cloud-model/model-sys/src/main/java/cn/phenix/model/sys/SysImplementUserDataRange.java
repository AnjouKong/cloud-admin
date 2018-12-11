package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 实施用户角色相关
 */
@Setter
@Getter
@Table(name = "SYS_IMPLEMENT_USER_DATA_RANGE")
@Entity
@DynamicInsert
@DynamicUpdate
public class SysImplementUserDataRange extends BaseModel {

    public static final String RANGE_ALL = "ALL";
    public static final String RANGE_ALLOT = "ALLOT";
    public static final String RANGE_FORBID = "FORBID";
    @Column
    private String userId;
    @Column
    private String roleRange;



}