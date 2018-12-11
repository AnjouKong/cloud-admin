package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsHtmlMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsHtml;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CmsHtmlService extends CoreService<CmsHtmlMapper, CmsHtml> {


    public PageTable<CmsHtml> findPage(DataTableParameter parameter, CmsHtml cmsHtml) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsHtml ");
        finder.whereExcludeDel();
        if ("1".equals(cmsHtml.getIsDisabled())) {
            finder.search("disabled", cmsHtml.isDisabled());
        }
        finder.search("channelId", cmsHtml.getChannelId());
        finder.search("name", cmsHtml.getName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc ");
        Page<CmsHtml> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
