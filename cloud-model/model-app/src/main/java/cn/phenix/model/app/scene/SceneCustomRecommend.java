package cn.phenix.model.app.scene;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;


/**
 * 自定义推荐位
 */

@Getter
@Setter
//@Entity
//@Table(name = "scene_custom_recommend")
public class SceneCustomRecommend extends BaseModel {

    public static final String modelName = "cn.phenix.model.app.scene.SceneCustomRecommend";
    /**
     * 名称
     */
    private String name;

    /**
     *  mongodb数据Id
     */
    private String recommendId;

}
