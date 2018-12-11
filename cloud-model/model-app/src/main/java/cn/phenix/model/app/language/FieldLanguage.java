package cn.phenix.model.app.language;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 字段多语言设置 fxq
 */
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "APP_LANGUAGE_FIELD_DIC")
public class FieldLanguage extends BaseModel {

    /**
     * 字段code
     */
    @Column(length = 50)
    private String fieldCode;

    /**
     * 语言code
     */
    @Column(length = 50)
    private String languageCode;

    /**
     * 字段id
     */
    @Column(length = 50)
    private String fieldId;

    /**
     * 值
     */
    private String value;

    /**
     * modelId
     */
    @Column(length = 50)
    private String modelId;

}
