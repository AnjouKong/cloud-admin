package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsWebsiteMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsWebsite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CmsWebsiteService extends CoreService<CmsWebsiteMapper, CmsWebsite> {

    public PageTable<CmsWebsite> findPage(DataTableParameter parameter, CmsWebsite cmsWebsite) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsWebsite ");
        finder.whereExcludeDel();
        finder.search("channelId", cmsWebsite.getChannelId());
        if ("1".equals(cmsWebsite.getIsDisabled())) {
            finder.search("disabled", cmsWebsite.isDisabled());
        }
        finder.search("type", cmsWebsite.getType());
        finder.search("name", cmsWebsite.getName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc ");
        Page<CmsWebsite> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<CmsWebsite> findByType(String type) {
        Finder finder = Finder.create("from CmsWebsite ");
        finder.whereExcludeDel();
        finder.search("type", type);
        finder.append(" order by updateDate desc ");
        return dao.find(finder);
    }


}
