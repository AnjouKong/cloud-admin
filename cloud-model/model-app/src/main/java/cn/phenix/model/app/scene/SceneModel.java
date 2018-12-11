package cn.phenix.model.app.scene;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 场景模块
 */

@Getter
@Setter
@Entity
@Table(name = "scene_model")
public class SceneModel extends BaseModel {

    /**
     * 模块名称
     */
    private String name;

    /**
     * 模块code
     */
    private String code;

    private boolean disabled;

    public static final String model_ui = "model_ui";

}
