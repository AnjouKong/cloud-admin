package cn.phenix.model.app.scene;

import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.model.app.language.LanguageDic;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 场景多语言关联
 */

@Getter
@Setter
@Entity
@Table(name = "scene_language")
public class SceneLanguage extends BaseModel {

    /**
     * 场景Id
     */
    @Column(length = 50)
    private String sceneId;

    /**
     * 语言id
     */
    @Column(length = 50)
    private String languageCode;

    @Transient
    private LanguageDic languageDic;

}
