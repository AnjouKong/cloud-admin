package cn.phenix.model.app.brand;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 设备品牌
*
 *  */

@Getter
@Setter
@Entity
@Table(name="APP_BRAND_MAIN")
@DynamicUpdate
@DynamicInsert
public class Brand extends BaseModel {
    //品牌名称
    @Column
    private String brandName;
    //品牌供应商
    @Column
    private String provider;
}
