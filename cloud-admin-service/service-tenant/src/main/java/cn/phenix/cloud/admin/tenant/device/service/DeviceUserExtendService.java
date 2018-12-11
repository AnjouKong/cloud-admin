package cn.phenix.cloud.admin.tenant.device.service;


import cn.phenix.cloud.admin.tenant.device.dao.DeviceUserExtendMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.tenant.device.DeviceUserExtends;
import cn.phenix.model.tenant.renter.Tenancy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
@Transactional(readOnly = true)
public class DeviceUserExtendService extends CoreService<DeviceUserExtendMapper, DeviceUserExtends> {

    public List<DeviceUserExtends> findBySpecificationId(String id) {

        return dao.findBySpecificationId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceUserProxy(Tenancy tenancy) {
        dao.updateByNamedQuery(Parameter.create("updateDeviceUserProxy").
                insert("hasPrivate", tenancy.getHasPrivate())
                .insert("ip", tenancy.getIp())
                .insert("playPort", tenancy.getPlayPort())
                .insert("proxyPort", tenancy.getProxyPort())
                .insert("resourceProxyPort", tenancy.getResourceProxyPort())
                .insert("id", tenancy.getId())
        );
    }

}
