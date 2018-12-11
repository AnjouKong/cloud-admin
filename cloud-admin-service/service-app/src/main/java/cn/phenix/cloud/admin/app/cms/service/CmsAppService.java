package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsAppMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CmsAppService extends CoreService<CmsAppMapper, CmsApp> {

    public PageTable<CmsApp> findPage(DataTableParameter parameter, CmsApp cmsApp) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsApp ");
        finder.whereExcludeDel();
        finder.search("channelId", cmsApp.getChannelId());
        if ("1".equals(cmsApp.getIsDisabled())) {
            finder.search("disabled", cmsApp.isDisabled());
        }
        finder.search("name", cmsApp.getName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc ");
        Page<CmsApp> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
