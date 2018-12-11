package cn.phenix.model.upgrade;

import cn.phenix.cloud.core.utils.DateUtils;
import cn.phenix.model.tenant.base.TenantBaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 终端更新记录
 *
 * @author xiaobin
 * @create 2018-01-08 下午1:37
 **/
@Getter
@Setter
@Table(name = "t_upgrade_launcher_record")
@Entity
public class UpgradeRecord extends TenantBaseModel {

    //终端Id
    @Column(length = 50)
    private String deviceId;

    //应用名称
    @Column(length = 100)
    private String launcherName;

    //应用包名
    @Column(length = 100)
    private String packageName;

    //应用版本
    @Column(length = 10)
    private String launcherVersion;

    //应用版本名称
    @Column(length = 20)
    private String launcherVersionName;

    //App 更新附件
    @Column(length = 500)
    private String upgradeAtt;

    //对应更新规则Id
    @Column(length = 38)
    private String upgradeStrategyId;

    //对应更新版本Id
    @Column(length = 38)
    private String upgradeVersionId;

    //升级状态
    @Column(length = 200)
    private String status;

    //失败原因
    @Column(length = 2000)
    private String failureMsg;

    @Transient
    public String getCreateDateStr(){
        return DateUtils.format(this.getCreateDate(),"yyyy-MM-dd HH:mm:ss");
    }

    //MODEL_ID=ID
    //MODEL_NAME=cn.phenix.model.app.upgrade.LauncherVersion
    //FD_KEY=version

    //更新附件
    /*@Transient
    public String getUpgradeAtt(){

    }*/
}
