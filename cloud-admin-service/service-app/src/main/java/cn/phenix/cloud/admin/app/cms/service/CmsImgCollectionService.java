package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsImgCollectionMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsImgCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CmsImgCollectionService extends CoreService<CmsImgCollectionMapper, CmsImgCollection> {


    public PageTable<CmsImgCollection> findPage(DataTableParameter parameter, CmsImgCollection cmsImgCollection) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsImgCollection ");
        finder.whereExcludeDel();
        if ("1".equals(cmsImgCollection.getIsDisabled())) {
            finder.search("disabled", cmsImgCollection.isDisabled());
        }
        finder.search("channelId", cmsImgCollection.getChannelId());
        finder.search("name", cmsImgCollection.getName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc ");
        Page<CmsImgCollection> page = dao.find(finder, pageRequest);

        return new PageTable<>(page);
    }


}
