package cn.phenix.cloud.admin.app.scene.basic.service;//package cn.phenix.cloud.admin.tenant.verify.service;


import cn.phenix.cloud.admin.app.scene.basic.dao.SceneBasicInfoEditorMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.app.scene.SceneBasicInfoEditor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class SceneBasicInfoEditorService extends CoreService<SceneBasicInfoEditorMapper, SceneBasicInfoEditor> {


    @Transactional
    public void saveAll(List<SceneBasicInfoEditor> editorList) {
        dao.saveAll(editorList);
    }

    @Transactional
    public void updateFlagByTemplateId(String templateId) {
        dao.updateFlagByTemplateId(templateId);
    }

    @Transactional
    public void deleteByTemplateId(String templateId) {
        dao.deleteByTemplateId(templateId);
    }

    public List<SceneBasicInfoEditor> findByTemplateId(String templateId) {
        return dao.findByTemplateIdAndDelFlag(templateId, SceneBasicInfoEditor.DEL_FLAG_NORMAL);
    }

    public List<SceneBasicInfoEditor> findByEditorId(String editorId) {
        return dao.findByEditorIdAndDelFlag(editorId, SceneBasicInfoEditor.DEL_FLAG_NORMAL);
    }

    public boolean isEditor(String editorId, String templateId) {
        List<SceneBasicInfoEditor> list = dao.findByEditorIdAndTemplateIdAndDelFlag(editorId, templateId, SceneBasicInfoEditor.DEL_FLAG_NORMAL);
        if (list != null && list.size() > 0) return true;
        return false;
    }

}
