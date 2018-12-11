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
 * 设备规格对应功能列表
 *
 **/

@Getter
@Setter
@Entity
@Table(name="APP_BRAND_SPECIFICATION_FUNC")
@DynamicUpdate
@DynamicInsert
public class BrandSpecificationFunc extends BaseModel {

    //功能名称
    @Column
    private String funcName;
    //功能归属
    @Column
    private String parentId;
    //功能id
    @Column
    private String funcId;
    //型号id
    @Column
    private String specificationId;


}
