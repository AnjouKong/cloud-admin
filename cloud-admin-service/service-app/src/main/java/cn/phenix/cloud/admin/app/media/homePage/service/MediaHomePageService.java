package cn.phenix.cloud.admin.app.media.homePage.service;

import cn.phenix.cloud.admin.app.media.homePage.dao.MediaHomePageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaHomePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class MediaHomePageService extends CoreService<MediaHomePageMapper, MediaHomePage> {

    public PageTable<MediaHomePage> findPage(DataTableParameter parameter, MediaHomePage homePage) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from MediaHomePage");
        finder.whereExcludeDel();
        finder.search("homePageName", homePage.getHomePageName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc");
        Page<MediaHomePage> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
