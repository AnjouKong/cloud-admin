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
 * 应用--多语言关联表 fxq
 */
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "APP_LANGUAGE_APPLICATION_DIC")
public class ApplicationLanguage extends BaseModel {

    /**
     * 应用Code
     */
    @Column(length = 50)
    private String applicationCode;

    /**
     * 语言code
     */
    @Column(length = 50)
    private String languageCode;

    /**
     * 应用id
     */
    @Column(length = 50)
    private String applicationId;

}
