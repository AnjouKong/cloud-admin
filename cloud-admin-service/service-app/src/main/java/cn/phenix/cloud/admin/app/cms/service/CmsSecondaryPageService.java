
package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsSecondaryPageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsSecondaryPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dwp on 2018/9/27.
 */

@Service
@Transactional(readOnly = true)
public class CmsSecondaryPageService extends CoreService<CmsSecondaryPageMapper, CmsSecondaryPage>{
    public PageTable<CmsSecondaryPage> findPage(DataTableParameter parameter, CmsSecondaryPage cmsSecondaryPage) {
            PageRequest pageRequest = parameter.getPageRequest();
            Finder finder = Finder.create("from CmsSecondaryPage ");
            finder.whereExcludeDel();
            finder.search("name", cmsSecondaryPage.getName(), Finder.SearchType.LIKE);
            finder.append(" order by updateDate desc ");
            Page<CmsSecondaryPage> page = dao.find(finder, pageRequest);
            return new PageTable<>(page);
        }
}

