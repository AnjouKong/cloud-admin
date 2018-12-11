package cn.phenix.cloud.admin.app.newMedia.media.service;

import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaChargeStrategyMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaChargeStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class NewMediaChargeStrategyService extends CoreService<NewMediaChargeStrategyMapper, NewMediaChargeStrategy> {


    public PageTable<NewMediaChargeStrategy> findPage(DataTableParameter parameter, NewMediaChargeStrategy newMediaChargeStrategy) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from NewMediaChargeStrategy ");
        finder.whereExcludeDel();
        finder.search("name", newMediaChargeStrategy.getName());

        Page<NewMediaChargeStrategy> page = dao.find(finder, pageRequest);

        return new PageTable<>(page);
    }


}
