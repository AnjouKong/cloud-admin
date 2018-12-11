package cn.phenix.model.app.brand;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 设备品牌规格
 *
 * */

@Getter
@Setter
@Entity
@Table(name="APP_BRAND_SPECIFICATION")
@DynamicUpdate
@DynamicInsert
public class BrandSpecification extends BaseModel {
    //型号名称
    @Column
    private String specificationName;
    //所属品牌
    @Column
    private String brandId;
    //型号功能
    @Column
    private String funcIds;

    @Transient
    private String brandName;

}
