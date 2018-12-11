package cn.phenix.model.upgrade;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
*
 **/
@Getter
@Setter
@Table(name = "t_upgrade_strategy_brand_spec")
@Entity
@DynamicUpdate
@DynamicInsert
public class StrategyBrandSpec extends BaseModel {

    private String brandId;
    private String specificationId;
    private String name;
    private String upgradeStrategyId;
}
