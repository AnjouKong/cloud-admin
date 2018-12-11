package cn.phenix.model.app.newMedia;


import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 媒资分类
 * Created by fxq
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="MEDIA_CATEGORY")
public class NewMediaCategory extends BaseModel {

    private static final long serialVersionUID = -4986525760385722162L;
    //分类名称
    @Column
    private String categoryName;
    /* //图片
     @Column
     private String categoryImg;*/
    //颜色
    @Column
    private String categoryColour;

    //描述
    @Column
    private String remarks;

    /**
     * 排序
     */
    @Column(name = "index_")
    private Integer index;

    /**
     * 是否发布
     */
    @Column
    private Boolean publish;
    /**
     * 分类类型 1为本地Media库分类 其他为CIBN原有分类
     */
    @Column
    private Integer categoryType;





}
