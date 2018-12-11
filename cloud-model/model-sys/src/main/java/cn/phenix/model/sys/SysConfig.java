package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
@Getter
@Setter
@Table(name = "sys_config")
public class SysConfig extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 100)
    private String configKey;

    @Column(length = 100)
    private String configValue;

    @Column
    private String note;

}
