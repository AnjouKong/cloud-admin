package cn.phenix.cloud.admin.tenant.device.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.device.DeviceVersion;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * Created by wizzer on 2016/12/22.
 */
public interface DeviceVersionMapper extends GenericJpaRepository<DeviceVersion,String> {

    @Modifying
    @Query("update DeviceVersion set delFlag='1' where applicationId=:applicationId ")
    void updateDelFlagByApplication(@Param("applicationId")String applicationId);

}

