package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsAdvertiseMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsAdvertise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class CmsAdvertiseService extends CoreService<CmsAdvertiseMapper, CmsAdvertise> {

    public PageTable<CmsAdvertise> findPage(DataTableParameter parameter, CmsAdvertise cmsAdvertise) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsAdvertise ");
        finder.whereExcludeDel();

        finder.search("channelId", cmsAdvertise.getChannelId());
        finder.search("name", cmsAdvertise.getName(), Finder.SearchType.LIKE);
        finder.search("resourceType", cmsAdvertise.getResourceType());

        if (Objects.equals(cmsAdvertise.getIsDisabled(), "1")) {
            finder.search("disabled", cmsAdvertise.isDisabled());
        }
        finder.append(" order by updateDate desc ");
        Page<CmsAdvertise> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
