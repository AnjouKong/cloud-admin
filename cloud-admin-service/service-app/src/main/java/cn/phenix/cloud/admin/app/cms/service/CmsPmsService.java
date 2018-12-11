package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsPmsMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsPms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CmsPmsService extends CoreService<CmsPmsMapper,CmsPms> {


    public PageTable<CmsPms> findPage(DataTableParameter parameter, CmsPms cmsPms) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsPms ");
        finder.whereExcludeDel();
        finder.search("channelId",cmsPms.getChannelId());
        finder.search("name",cmsPms.getName(),Finder.SearchType.LIKE);
        finder.append(" order by updateDate desc ");
        Page<CmsPms> page = dao.find(finder,pageRequest);
        return new PageTable<>(page);
    }


}
