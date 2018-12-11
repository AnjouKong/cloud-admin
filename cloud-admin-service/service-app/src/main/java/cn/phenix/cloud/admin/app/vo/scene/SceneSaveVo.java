package cn.phenix.cloud.admin.app.vo.scene;

import cn.phenix.model.ui.v1.launcher.AppUI;
import lombok.Getter;
import lombok.Setter;

/**
 * mongodbId保存vo
 **/
@Getter
@Setter
public class SceneSaveVo{

    private String id;
    private AppUI appUI;
    private String sceneBasicInfoId;
    private String version;

}
