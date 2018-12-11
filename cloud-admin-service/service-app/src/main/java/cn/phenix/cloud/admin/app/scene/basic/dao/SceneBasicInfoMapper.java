package cn.phenix.cloud.admin.app.scene.basic.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.scene.SceneBasicInfo;

public interface SceneBasicInfoMapper extends GenericJpaRepository<SceneBasicInfo,String> {

    String findSceneBasicInfo="findSceneBasicInfo";
}
