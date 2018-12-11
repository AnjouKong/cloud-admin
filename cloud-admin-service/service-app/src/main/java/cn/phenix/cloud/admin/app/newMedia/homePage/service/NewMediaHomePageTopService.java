package cn.phenix.cloud.admin.app.newMedia.homePage.service;

import cn.phenix.cloud.admin.app.newMedia.homePage.dao.NewMediaHomePageTopMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaHomePageTop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class NewMediaHomePageTopService extends CoreService<NewMediaHomePageTopMapper, NewMediaHomePageTop> {

    public PageTable<NewMediaHomePageTop> findPage(DataTableParameter parameter, NewMediaHomePageTop mediaHomePageTop) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from NewMediaHomePageTop");
        finder.whereExcludeDel();
        finder.search("homePageId", mediaHomePageTop.getHomePageId());
        finder.append(" order by location");
        Page<NewMediaHomePageTop> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
