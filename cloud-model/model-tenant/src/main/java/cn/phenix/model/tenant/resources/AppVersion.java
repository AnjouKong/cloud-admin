package cn.phenix.model.tenant.resources;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by xiaobin on 2017/8/25.
 */
@Getter
@Setter
@Entity
@Table(name="APP_APP_VERSION")
public class AppVersion extends BaseModel {

    @Column
    private String versionId;

    @Column
    private String versionName;
}
