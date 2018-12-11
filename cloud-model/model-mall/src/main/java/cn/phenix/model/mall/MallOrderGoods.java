package cn.phenix.model.mall;

import cn.phenix.model.tenant.base.TenantBaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 订单和商品的关联信息
 *
 * @author xiaobin
 * @create 2017-11-01 下午1:17
 **/
@Getter
@Setter
@Entity
@Table(name = "MALL_ORDER_GOODS")
public class MallOrderGoods extends TenantBaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private MallOrder order;
    @Column
    private String goodId;

    @Column
    private String goodName;


    //商品类型
    @Column
    private String goodType;

    /**
     * 商品价格
     */
    @Column(precision = 9,scale = 2)
    private BigDecimal price;

    //购买数量
    @Column
    private Integer goodsNum;

    private String tenantId;

}
