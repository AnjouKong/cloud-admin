package cn.phenix.model.app.newMedia;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * 媒资价格策略
 * Created by fxq
 */
@Getter
@Setter
@Entity
@Table(name = "media_charge_strategy")
public class NewMediaChargeStrategy extends BaseModel {

    /**
     * 策略名称
     */
    @Column(length = 100)
    private String name;

    /**
     * 计算公式
     */
    @Column
    private String formula;

    /**
     * 默认价格 （公式计算错误或者出现负数时使用此价格） 单位 分
     */
    @Column(length = 10)
    private Integer charge;


    @Transient
    public double getShowCharge() {
        if (this.getCharge() == null) return 0;
        return new BigDecimal(this.getCharge().doubleValue() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
