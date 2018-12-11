package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsMusicMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsMusic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CmsMusicService extends CoreService<CmsMusicMapper, CmsMusic> {


    public PageTable<CmsMusic> findPage(DataTableParameter parameter, CmsMusic cmsMusic) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsMusic ");
        finder.whereExcludeDel();
        if ("1".equals(cmsMusic.getIsDisabled())) {
            finder.search("disabled", cmsMusic.isDisabled());
        }
        finder.search("channelId", cmsMusic.getChannelId());
        finder.search("name", cmsMusic.getName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc ");
        Page<CmsMusic> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
