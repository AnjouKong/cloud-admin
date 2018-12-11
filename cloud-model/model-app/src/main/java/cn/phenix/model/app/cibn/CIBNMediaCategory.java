package cn.phenix.model.app.cibn;


import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 媒资分类
 */
@Getter
@Setter
@Entity
@Table(name="APP_CIBN_CATEGORY")
public class CIBNMediaCategory extends BaseModel {

    //分类名称
    @Column
    private String categoryName;
}
