package cn.phenix.model.app.scene;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 *  模板可读权限
 */

@Getter
@Setter
@Entity
@Table(name = "scene_basic_info_template_reader")
public class SceneBasicInfoReader extends BaseModel {

    /**
     * 模板Id
     */
    private String templateId;

    /**
     * 可读用户Id
     */
    private String readerId;

}
