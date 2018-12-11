package cn.phenix.cloud.admin.app.newMedia.media.service;

import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaChargeMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.newMedia.NewMediaCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class NewMediaChargeService extends CoreService<NewMediaChargeMapper, NewMediaCharge> {


    public PageTable<NewMediaCharge> findPage(DataTableParameter parameter, NewMediaCharge newMediaCharge) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from NewMediaCharge  ");
        finder.whereExcludeDel();
        Page<NewMediaCharge> page = dao.find(finder, pageRequest);

        return new PageTable<>(page);
    }


}
