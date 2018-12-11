package cn.phenix.cloud.admin.app.media.homePage.service;

import cn.phenix.cloud.admin.app.media.homePage.dao.MediaHomePageTopMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaHomePageTop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class MediaHomePageTopService extends CoreService<MediaHomePageTopMapper, MediaHomePageTop> {

    public PageTable<MediaHomePageTop> findPage(DataTableParameter parameter, MediaHomePageTop mediaHomePageTop) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from MediaHomePageTop");
        finder.whereExcludeDel();
        finder.search("homePageId", mediaHomePageTop.getHomePageId());
        finder.append(" order by location");
        Page<MediaHomePageTop> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
