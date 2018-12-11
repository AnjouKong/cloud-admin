package cn.phenix.cloud.admin.app.newMedia.media.service;

import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class NewMediaService extends CoreService<NewMediaMapper, NewMedia> {


    public PageTable<NewMedia> findPage(String seriesID, String programCode, String programVolumnCount, DataTableParameter parameter) {

        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from NewMedia where 1=1");
        finder.search("seriesID", seriesID, Finder.SearchType.EQ);
        finder.search("programCode", programCode, Finder.SearchType.EQ);
        finder.search("programVolumnCount", programVolumnCount, Finder.SearchType.EQ);

        Page<NewMedia> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
