package cn.phenix.cloud.admin.tenant.pms.service;

import cn.phenix.cloud.admin.tenant.pms.dao.CheckInMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.pms.CheckIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
@Transactional(readOnly = true)
public class CheckInService extends CoreService<CheckInMapper, CheckIn> {


    @Autowired
    private CheckInMapper checkInMapper;

    public PageTable<CheckIn> findPage(DataTableParameter parameter, CheckIn checkIn) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CheckIn ");
        finder.whereExcludeDel();
        Page<CheckIn> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }



}
