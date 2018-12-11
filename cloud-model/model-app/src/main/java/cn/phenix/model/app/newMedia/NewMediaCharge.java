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
 * 媒资价格关联表
 * 用于特殊修改的价格存储
 * Created by fxq
 */
@Getter
@Setter
@Entity
@Table(name = "media_charge")
public class NewMediaCharge extends BaseModel {

    /**
     * 媒资Id
     */
    @Column(length = 100)
    private String mediaId;

    /**
     * 策略Id
     */
    @Column(length = 100)
    private String strategyId;

    /**
     * 价格 单位 分
     */
    @Column(length = 10)
    private Integer charge;

    @Transient
    public double getShowCharge() {
        if (this.getCharge() == null) return 0;
        return new BigDecimal(this.getCharge().doubleValue() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
