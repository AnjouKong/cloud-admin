package cn.phenix.model.upgrade;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
*
 **/
@Getter
@Setter
@Table(name = "t_upgrade_strategy_deviceGroup")
@Entity
@DynamicUpdate
@DynamicInsert
public class StrategyDeviceGroup extends BaseModel {

    private String tenantId;//商户id
    private String groupId;//设备组id
    private String name;//名称
    private String upgradeStrategyId;//策略id
}
