
package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsCustomMapper;
import cn.phenix.cloud.admin.app.cms.dao.CmsThemePackageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsCustom;
import cn.phenix.model.app.cms.CmsThemePackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dwp on 2018/9/27.
 */

@Service
@Transactional(readOnly = true)
public class CmsCustomService extends CoreService<CmsCustomMapper, CmsCustom>{
    public PageTable<CmsCustom> findPage(DataTableParameter parameter, CmsCustom cmsCustom) {
            PageRequest pageRequest = parameter.getPageRequest();
            Finder finder = Finder.create("from CmsCustom ");
            finder.whereExcludeDel();
            finder.search("name", cmsCustom.getName(), Finder.SearchType.LIKE);
            finder.append(" order by updateDate desc ");
            Page<CmsCustom> page = dao.find(finder, pageRequest);
            return new PageTable<>(page);
        }
}

