package cn.phenix.model.upgrade;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * launcher版本依赖
 *
 **/
@Getter
@Setter
@Table(name = "t_upgrade_launcher_version_depend")
@Entity
@DynamicUpdate
@DynamicInsert
public class LauncherVersionDepend extends BaseModel {

    //版本id（即新建版本的id）
    private String versionId;
    //依赖id(依赖于此版本的versionId)
    private String dependVersionId;

    private String dependLauncherName;
    private String dependLauncherId;
    private String dependVersionName;
}
