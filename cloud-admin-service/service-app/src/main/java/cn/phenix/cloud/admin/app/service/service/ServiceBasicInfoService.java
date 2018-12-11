package cn.phenix.cloud.admin.app.service.service;

import cn.phenix.cloud.admin.app.service.dao.ServiceBasicInfoMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.service.ServiceBasicInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ServiceBasicInfoService extends CoreService<ServiceBasicInfoMapper, ServiceBasicInfo> {


    public PageTable<ServiceBasicInfo> findPage(DataTableParameter parameter, ServiceBasicInfo serviceBasicInfo) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from ServiceBasicInfo ");
        finder.whereExcludeDel();
        finder.search("serviceName", serviceBasicInfo.getServiceName(), Finder.SearchType.LIKE);
        Page<ServiceBasicInfo> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<ServiceBasicInfo> findAll() {
        Finder finder = Finder.create("from ServiceBasicInfo ");
        finder.whereExcludeDel();
        List<ServiceBasicInfo> list = dao.find(finder);
        return list;
    }

}
