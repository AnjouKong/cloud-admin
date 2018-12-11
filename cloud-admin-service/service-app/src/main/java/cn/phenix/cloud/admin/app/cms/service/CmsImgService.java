package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsImgMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CmsImgService extends CoreService<CmsImgMapper, CmsImg> {


    public PageTable<CmsImg> findPage(DataTableParameter parameter, CmsImg cmsImg) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsImg ");
        finder.whereExcludeDel();
        if ("1".equals(cmsImg.getIsDisabled())) {
            finder.search("disabled", cmsImg.isDisabled());
        }
        finder.search("channelId", cmsImg.getChannelId());
        finder.search("name", cmsImg.getName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc ");
        Page<CmsImg> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
