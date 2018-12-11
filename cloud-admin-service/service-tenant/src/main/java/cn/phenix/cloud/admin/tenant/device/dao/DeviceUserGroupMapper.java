package cn.phenix.cloud.admin.tenant.device.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.device.DeviceUserGroup;

import java.util.List;


/**
 * Created by wizzer on 2016/12/22.
 */
public interface DeviceUserGroupMapper extends GenericJpaRepository<DeviceUserGroup,String> {


    DeviceUserGroup findByIdAndDelFlag(String id, String s);

    List<DeviceUserGroup> findByDelFlagAndTenantId(String delFlag, String id);

    List<DeviceUserGroup> findByDelFlag(String s);
}

