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
*
 **/
@Getter
@Setter
@Table(name = "t_upgrade_strategy_city")
@Entity
@DynamicUpdate
@DynamicInsert
public class StrategyCity extends BaseModel {

    private String provinceId;
    private String cityId;
    private String name;
    private String areaCode;
    private String upgradeStrategyId;
}
