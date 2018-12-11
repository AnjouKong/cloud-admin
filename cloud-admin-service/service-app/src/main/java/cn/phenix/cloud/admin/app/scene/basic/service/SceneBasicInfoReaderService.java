package cn.phenix.cloud.admin.app.scene.basic.service;//package cn.phenix.cloud.admin.tenant.verify.service;


import cn.phenix.cloud.admin.app.scene.basic.dao.SceneBasicInfoReaderMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.app.scene.SceneBasicInfoReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class SceneBasicInfoReaderService extends CoreService<SceneBasicInfoReaderMapper, SceneBasicInfoReader> {

    @Transactional
    public void saveAll(List<SceneBasicInfoReader> readerList) {
        dao.saveAll(readerList);
    }


    @Transactional
    public void updateFlagByTemplateId(String templateId) {
        dao.updateFlagByTemplateId(templateId);
    }

    @Transactional
    public void deleteByTemplateId(String templateId) {
        dao.deleteByTemplateId(templateId);
    }

    public List<SceneBasicInfoReader> findByTemplateId(String templateId) {
        return dao.findByTemplateIdAndDelFlag(templateId, SceneBasicInfoReader.DEL_FLAG_NORMAL);
    }

    public List<SceneBasicInfoReader> findByReaderId(String readerId) {
        return  dao.findByReaderIdAndDelFlag(readerId, SceneBasicInfoReader.DEL_FLAG_NORMAL);
    }


    public boolean isReader(String readerId, String templateId) {
        List<SceneBasicInfoReader> list = dao.findByReaderIdAndTemplateIdAndDelFlag(readerId, templateId, SceneBasicInfoReader.DEL_FLAG_NORMAL);
        if (list != null && list.size() > 0) return true;
        return false;
    }

}
