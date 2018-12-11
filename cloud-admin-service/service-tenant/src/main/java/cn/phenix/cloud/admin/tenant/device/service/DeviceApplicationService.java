package cn.phenix.cloud.admin.tenant.device.service;


import cn.phenix.cloud.admin.tenant.device.dao.DeviceApplicationMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.device.DeviceApplication;
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
public class DeviceApplicationService extends CoreService<DeviceApplicationMapper, DeviceApplication> {
    @Autowired
    private DeviceApplicationMapper deviceApplicationMapper;


    public PageTable<DeviceApplication> findPage(DataTableParameter parameter, DeviceApplication deviceApplication) {

        PageRequest pageRequest =parameter.getPageRequest();
        Finder finder =  Finder.create("from DeviceApplication ");
        finder.whereExcludeDel();
        finder.search("applicationName",deviceApplication.getApplicationName(),Finder.SearchType.LIKE);

        Page<DeviceApplication> page = dao.find(finder,pageRequest);
        return new PageTable<>(page);
    }


}
