package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsResourcesLanguageMapper;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsResourcesLanguage;
import cn.phenix.model.app.language.LanguageDic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CmsResourcesLanguageService extends CoreService<CmsResourcesLanguageMapper, CmsResourcesLanguage> {


    @Autowired
    private LanguageDicService languageDicService;

    public PageTable<CmsResourcesLanguage> findPage(DataTableParameter parameter, CmsResourcesLanguage cmsResourcesLanguage) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsResourcesLanguage ");
        finder.whereExcludeDel();
        finder.search("resourcesType", cmsResourcesLanguage.getResourcesType());
        finder.search("resourcesId", cmsResourcesLanguage.getResourcesId());
        Page<CmsResourcesLanguage> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<CmsResourcesLanguage> findResources(String resourcesId, String resourcesType) {
        Finder finder = Finder.create("from CmsResourcesLanguage ");
        finder.whereExcludeDel();
        finder.search("resourcesId", resourcesId);
        finder.search("resourcesType", resourcesType);
        List<CmsResourcesLanguage> list = dao.find(finder);
        int i = 1;
        for (CmsResourcesLanguage r : list) {
            LanguageDic languageDic=languageDicService.findByCodeAndDelFlag(r.getLanguage());
            r.setLanguageName(languageDic.getName());
            r.setNum(i);
            i++;
        }
        return list;
    }
    public List<CmsResourcesLanguage> findByResourceId(String resourceId) {
        Finder finder = Finder.create("from CmsResourcesLanguage ");
        finder.whereExcludeDel();
        finder.search("resourcesId", resourceId);
        List<CmsResourcesLanguage> list = dao.find(finder);
        for (CmsResourcesLanguage r : list) {
            r.setLanguageDic(languageDicService.findByCodeAndDelFlag(r.getLanguage()));
        }
        return list;
    }

    @Transactional
    public void deleteResources(String resourcesId, String resourcesType) {
        List<CmsResourcesLanguage> list = findResources(resourcesId, resourcesType);
        if (list == null) {
            return;
        }
        dao.deleteAll(list);
    }

}
