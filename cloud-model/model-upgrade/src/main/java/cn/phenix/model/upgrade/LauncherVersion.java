package cn.phenix.model.upgrade;

import cn.phenix.cloud.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * launcher版本
 *
 * @author xiaobin
 * @create 2018-01-02 下午6:39
 **/
@Getter
@Setter
@Table(name = "t_upgrade_launcher_version")
@Entity
@DynamicUpdate
@DynamicInsert
public class LauncherVersion extends BaseModel {                        

    //MODEL_ID=ID
    //MODEL_NAME=cn.phenix.model.app.upgrade.LauncherVersion
    //FD_KEY=version

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "launcherId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private Launcher launcher;

    public static final String MODEL_NAME = "cn.phenix.model.app.upgrade.LauncherVersion";

    //版本
    private String version;

    //版本名称
    private String versionName;

//    //策略id
    private String strategyId;
//
//    //版本id
//    private String launcherId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "launcherVersionId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<UpgradeStrategy> strategyList= Lists.newArrayList();
}
