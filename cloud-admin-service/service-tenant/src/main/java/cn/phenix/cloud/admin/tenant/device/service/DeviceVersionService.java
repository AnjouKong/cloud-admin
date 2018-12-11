package cn.phenix.cloud.admin.tenant.device.service;


import cn.phenix.cloud.admin.tenant.device.dao.DeviceVersionMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.device.DeviceVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
@Transactional(readOnly = true)
public class DeviceVersionService extends CoreService<DeviceVersionMapper, DeviceVersion> {
    @Autowired
    private DeviceVersionMapper deviceVersionMapper;


    public PageTable<DeviceVersion> findPage(DataTableParameter parameter, DeviceVersion deviceVersion) {

        PageRequest pageRequest =parameter.getPageRequest();
        Finder finder =  Finder.create("from DeviceVersion ");
        finder.whereExcludeDel();
        finder.search("applicationId",deviceVersion.getApplicationId());

        Page<DeviceVersion> page = dao.find(finder,pageRequest);
        return new PageTable<>(page);
    }


    public List<DeviceVersion> findByApplicationId(String applicationId){
        Finder finder=Finder.create("from DeviceVersion ");
        finder.whereExcludeDel();
        finder.search("applicationId",applicationId);
        return dao.find(finder);
    }


    @Transactional
    public void updateDelFlagByApplication(String applicationId){
        deviceVersionMapper.updateDelFlagByApplication(applicationId);
    }

}
