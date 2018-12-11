package cn.phenix.model.tenant.vip;

import cn.phenix.cloud.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author socilents
 * Create in 2018/5/24 11:41
 **/
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="APP_PACKAGE_CONTENT")
public class VipContent extends BaseModel {
    private String contentName;
    private String price;
    private String originalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packageId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private VipPackage vipPackage;

    //时间单位，小时、天、周、月、季度、年
    private String timeUnit;
    //时间，整数：1,2,12....。
    private String time;
    //有效时间毫秒，3天=1000 * 60 * 60 * 24 * 3
    private String timeStrap;
    //是否推荐：0否1是
    private String isRecommend;
    @Transient
    private String packageName;
}
