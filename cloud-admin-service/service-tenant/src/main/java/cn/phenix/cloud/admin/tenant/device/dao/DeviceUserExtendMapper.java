package cn.phenix.cloud.admin.tenant.device.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.device.DeviceUserExtends;

import java.util.List;

public interface DeviceUserExtendMapper extends GenericJpaRepository<DeviceUserExtends,String> {

    String updateDeviceUserProxy="updateDeviceUserProxy";

    List<DeviceUserExtends> findBySpecificationId(String id);
}

