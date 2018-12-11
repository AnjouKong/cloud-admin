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
 * Created by wizzer on 2016/8/11.
 */
@Setter
@Getter
@Table(name = "sys_api")
@Entity
@DynamicInsert
@DynamicUpdate
public class SysApi extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 20)
    private String appName;

    @Column
    private String appId;

    @Column
    private String appSecret;


}
