package cn.phenix.cloud.admin.app.newMedia.homePage.service;

import cn.phenix.cloud.admin.app.newMedia.homePage.dao.NewMediaHomePageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaHomePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class NewMediaHomePageService extends CoreService<NewMediaHomePageMapper, NewMediaHomePage> {

    public PageTable<NewMediaHomePage> findPage(DataTableParameter parameter, NewMediaHomePage homePage) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from NewMediaHomePage");
        finder.whereExcludeDel();
        finder.search("homePageName", homePage.getHomePageName(), Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc");
        Page<NewMediaHomePage> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
