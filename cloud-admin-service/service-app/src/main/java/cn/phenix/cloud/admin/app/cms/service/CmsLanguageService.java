package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsLanguageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CmsLanguageService extends CoreService<CmsLanguageMapper,CmsLanguage> {


    public PageTable<CmsLanguage> findPage(DataTableParameter parameter, CmsLanguage cmsLanguage) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsLanguage ");
        finder.whereExcludeDel();
        finder.search("channelId",cmsLanguage.getChannelId());
        finder.search("name",cmsLanguage.getName(),Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc ");
        Page<CmsLanguage> page = dao.find(finder,pageRequest);
        return new PageTable<>(page);
    }


}
