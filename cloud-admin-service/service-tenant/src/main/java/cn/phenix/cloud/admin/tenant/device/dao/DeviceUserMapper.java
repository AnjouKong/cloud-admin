package cn.phenix.cloud.admin.tenant.device.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.device.DeviceUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Created by wizzer on 2016/12/22.
 */
public interface DeviceUserMapper extends GenericJpaRepository<DeviceUser,String> {

    List<DeviceUser> findByDeviceIdAndDelFlag(String deviceId,String delFlag);

    List<DeviceUser> findByTenantIdAndDelFlag(String tenantId,String delFlag);

    List<DeviceUser> findByDeviceGroupCodeAndDelFlag(String deviceGroupCode,String delFlag);

    @Modifying
    @Query(value="update DeviceUser set deviceGroupCode = '' where id = :id")
    void removeGroup(@Param("id")String id);

    @Modifying
    @Query(value="update DeviceUser set  testGroupId =:testGroupId where id in :id")
    void updateTestGroup(@Param("testGroupId")String testGroupId,@Param("id")String[] id);
}

