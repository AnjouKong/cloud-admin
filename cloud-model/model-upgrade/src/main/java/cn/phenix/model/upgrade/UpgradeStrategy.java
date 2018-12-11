package cn.phenix.model.upgrade;

import cn.phenix.cloud.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 更新策略
 *
 * @author xiaobin
 * @create 2018-01-02 下午6:43
 **/
@Getter
@Setter
@Table(name = "t_upgrade_strategy")
@Entity
@DynamicUpdate
@DynamicInsert
public class UpgradeStrategy extends BaseModel {

    //更新时间(小时)
    private String startTime;
    //更新时间（小时）
    private String endTime;

    //更新有效期(对应到天)
    private String effectiveStartDate;

    //更新有效期(对应到天)
    private String effectiveEndDate;

    //指定上一个版本
    private String lastVersion;

    //更新方式：1：强制；2：可取消；3：静默升级
    private String upgradeWay;

    /**
     * 存储状态(1:暂存；2：发布；3：废除)
     */
    private String fdStatus;
    /**
     * 升级方式(1:升级；2：卸载；)
     */
    private String promotionWay;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "launcherVersionId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private LauncherVersion launcherVersion;
}
