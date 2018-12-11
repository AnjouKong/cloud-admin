package cn.phenix.cloud.admin.app.scene.basic.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.scene.SceneBasicInfoReader;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SceneBasicInfoReaderMapper extends GenericJpaRepository<SceneBasicInfoReader, String> {

    @Modifying
    @Query("update SceneBasicInfoReader set delFlag ='1' where templateId = :templateId")
    void updateFlagByTemplateId(@Param("templateId") String templateId);

    @Modifying
    @Query("delete SceneBasicInfoReader where templateId = :templateId")
    void deleteByTemplateId(@Param("templateId") String templateId);

    List<SceneBasicInfoReader> findByTemplateIdAndDelFlag(String templateId, String delFlag);

    List<SceneBasicInfoReader> findByReaderIdAndDelFlag(String readerId, String delFlag);

    List<SceneBasicInfoReader> findByReaderIdAndTemplateIdAndDelFlag(String readerId, String templateId, String delFlag);
}
