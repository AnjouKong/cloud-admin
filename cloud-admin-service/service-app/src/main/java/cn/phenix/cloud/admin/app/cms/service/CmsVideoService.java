package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsVideoMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CmsVideoService extends CoreService<CmsVideoMapper, CmsVideo> {


    public PageTable<CmsVideo> findPage(DataTableParameter parameter, CmsVideo cmsVideo) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsVideo ");
        finder.whereExcludeDel();

        finder.search("channelId", cmsVideo.getChannelId());
        finder.search("name", cmsVideo.getName(), Finder.SearchType.LIKE);
        if (cmsVideo.getIsDisabled()=="1") {
            finder.search("disabled", cmsVideo.isDisabled());
        }
        finder.append(" order by updateDate desc ");
        Page<CmsVideo> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
