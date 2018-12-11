package cn.phenix.model.app.cibn;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 媒资标签
 */
@Getter
@Setter
@Entity
@Table(name="APP_CIBN_TAG")
public class CIBNMediaTag extends BaseModel {

    @Column
    private String tagName;
}
