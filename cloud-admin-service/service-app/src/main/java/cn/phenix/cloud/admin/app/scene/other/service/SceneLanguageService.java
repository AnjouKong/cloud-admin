package cn.phenix.cloud.admin.app.scene.other.service;//package cn.phenix.cloud.admin.tenant.verify.service;


import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.app.scene.other.dao.SceneLanguageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.model.app.scene.SceneLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class SceneLanguageService extends CoreService<SceneLanguageMapper, SceneLanguage> {

    @Autowired
    private LanguageDicService languageDicService;


    public List<SceneLanguage> findBySceneId(String sceneId) {
        Finder finder = Finder.create("from SceneLanguage ");
        finder.whereExcludeDel();
        finder.search("sceneId", sceneId);
        List<SceneLanguage> sceneLanguage = dao.find(finder);
        for (SceneLanguage s : sceneLanguage) {
            s.setLanguageDic(languageDicService.findByCodeAndDelFlag(s.getLanguageCode()));
        }
        return sceneLanguage;
    }

    public void deleteBySceneId(String sceneId) {
        List<SceneLanguage> sceneLanguage = findBySceneId(sceneId);
        if (sceneLanguage == null) {
            return;
        }
        dao.deleteAll(sceneLanguage);
    }


}
