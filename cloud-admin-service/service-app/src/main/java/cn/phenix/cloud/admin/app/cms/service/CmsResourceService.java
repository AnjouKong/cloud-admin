package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsResourceMapper;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CmsResourceService extends CoreService<CmsResourceMapper, CmsResource> {

    @Autowired
    private CmsResourcesLanguageService cmsResourcesLanguageService;
    @Autowired
    private CmsAdvertiseService cmsAdvertiseService;

    public PageTable<CmsResource> findPage(DataTableParameter parameter, CmsResource cmsResource) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsResource ");
        finder.whereExcludeDel();
        finder.search("name", cmsResource.getName(), Finder.SearchType.LIKE);
        finder.search("resourceType", cmsResource.getResourceType());
        finder.search("channelId", cmsResource.getChannelId());
        if ("1".equals(cmsResource.getIsDisabled())) {
            finder.search("disabled", false);
        }
        Page<CmsResource> page = dao.find(finder, pageRequest);
        for (CmsResource resource : page.getContent()) {
            List<CmsResourcesLanguage> language = cmsResourcesLanguageService.findByResourceId(resource.getId());
            String name = "";
            for (CmsResourcesLanguage c : language) {
                if (c.getLanguageDic() == null) continue;
                name += c.getLanguageDic().getName() + ",";
            }
            resource.setLanguageName(!Strings.isBlank(name) ? name.substring(0, name.length() - 1) : "");
            resource.setResourceTypeText(resource.getResourceType().getText());
            //图片预览
            String modelName = getModelName(resource.getResourceType().name());
            if (modelName != null) {
                resource.setResource(resource.getId() + "/" + modelName + "/");
            } else {
                resource.setResource("");
            }

            //验证是否为视频
            if (resource.getResourceType().name().equals("video")) {
                resource.setVideo(true);
                continue;
            }
            if (resource.getResourceType().name().equals("ad")) {
                CmsAdvertise adv = cmsAdvertiseService.get(resource.getId());
                if ("video".equals(adv.getFileType())) {
                    resource.setVideo(true);
                    continue;
                }
            }

        }
        return new PageTable<>(page);
    }


    private String getModelName(String type) {
        switch (type) {
            case "img":
                return CmsImg.modelName;
            case "imgCollection":
                return CmsImgCollection.modelName;
            case "pms":
                return CmsPms.modelName;
        }
        return null;
    }
}
