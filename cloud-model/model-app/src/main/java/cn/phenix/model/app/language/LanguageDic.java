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
 * 多语言字典 fxq
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "APP_LANGUAGE_DIC_MAIN")
public class LanguageDic extends BaseModel {
    /**
     * 名字
     */
    @Column(length = 50)
    private String name;

    /**
     * code
     */
    @Column(length = 50)
    private String code;
}
