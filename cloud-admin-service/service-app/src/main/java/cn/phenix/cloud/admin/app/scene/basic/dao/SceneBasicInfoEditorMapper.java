package cn.phenix.cloud.admin.app.scene.basic.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.scene.SceneBasicInfoEditor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SceneBasicInfoEditorMapper extends GenericJpaRepository<SceneBasicInfoEditor, String> {

    @Modifying
    @Query("update SceneBasicInfoEditor set delFlag ='1' where templateId = :templateId")
    void updateFlagByTemplateId(@Param("templateId") String templateId);

    @Modifying
    @Query("delete SceneBasicInfoEditor where templateId = :templateId")
    void deleteByTemplateId(@Param("templateId") String templateId);

    List<SceneBasicInfoEditor> findByTemplateIdAndDelFlag(String templateId, String delFlag);

    List<SceneBasicInfoEditor> findByEditorIdAndDelFlag(String editorId, String delFlag);

    List<SceneBasicInfoEditor> findByEditorIdAndTemplateIdAndDelFlag(String editorId, String templateId, String delFlag);

}
