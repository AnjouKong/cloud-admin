package cn.phenix.cloud.admin.app.scene.basic.service;//package cn.phenix.cloud.admin.tenant.verify.service;


import cn.phenix.cloud.admin.app.cms.service.CmsAdvertiseService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourceService;
import cn.phenix.cloud.admin.app.cms.service.CmsResourcesLanguageService;
import cn.phenix.cloud.admin.app.scene.basic.dao.SceneResourceMapper;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.model.app.cms.CmsAdvertise;
import cn.phenix.model.app.cms.CmsResource;
import cn.phenix.model.app.cms.CmsResourcesLanguage;
import cn.phenix.model.app.scene.SceneResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class SceneResourceService extends CoreService<SceneResourceMapper, SceneResource> {


    @Autowired
    private CmsResourceService cmsResourceService;
    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;
    @Autowired
    private CmsAdvertiseService cmsAdvertiseService;


    public List<SceneResource> findBySceneIdAndEventType(String sceneId, String eventType) {
        Finder finder = Finder.create("select r from SceneResource r , CmsResource cms ");
        finder.whereExcludeDel("r");
        finder.append(" and r.resourceId=cms.id ");
        finder.search("r.sceneId", sceneId);
        finder.search("r.eventType", eventType);
        finder.search("cms.disabled", false);
        finder.search("cms.delFlag", "0");

        finder.append(" order by r.index");
        List<SceneResource> advList = dao.find(finder);
        for (SceneResource resource : advList) {
            CmsResource cmsResource = cmsResourceService.get(resource.getResourceId());
            if (cmsResource == null) continue;
            resource.setResourceName(cmsResource.getName());
            resource.setResourceType(cmsResource.getResourceType().getText());

            List<CmsResourcesLanguage> language = cmsResourcesLanguageService.findByResourceId(resource.getResourceId());
            String name = "";
            for (CmsResourcesLanguage c : language) {
                if (c.getLanguageDic() == null) continue;
                name += c.getLanguageDic().getName() + ",";
            }
            resource.setLanguageName(!Strings.isBlank(name) ? name.substring(0, name.length() - 1) : "");

            //验证是否为视频
            if (cmsResource.getResourceType().name().equals("video")) {
                resource.setVideo(true);
                continue;
            }
            if (cmsResource.getResourceType().name().equals("ad")) {
                CmsAdvertise adv = cmsAdvertiseService.get(resource.getResourceId());
                if ("video".equals(adv.getFileType())) {
                    resource.setVideo(true);
                    continue;
                }
            }


        }
        return advList;
    }

    @Transactional
    public void updateBySceneId(String sceneId) {
        Finder finder = Finder.create("from SceneResource ");
        finder.whereExcludeDel();
        finder.search("sceneId", sceneId);
        List<SceneResource> advList = dao.find(finder);
        for (SceneResource s : advList) {
            deleteById(s.getId());
        }
    }

    @Transactional
    public void deleteBySceneId(String sceneId) {
        Finder finder = Finder.create("from SceneResource ");
        finder.whereExcludeDel();
        finder.search("sceneId", sceneId);
        List<SceneResource> advList = dao.find(finder);
        dao.deleteAll(advList);
    }

    @Transactional
    public void saveAll(List<SceneResource> sceneResourceList) {
        dao.saveAll(sceneResourceList);
    }
}
