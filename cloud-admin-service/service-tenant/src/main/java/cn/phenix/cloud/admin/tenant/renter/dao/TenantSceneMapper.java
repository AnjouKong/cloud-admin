package cn.phenix.cloud.admin.tenant.renter.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.renter.TenantScene;

import java.util.List;

public interface TenantSceneMapper extends GenericJpaRepository<TenantScene,String> {

    List<TenantScene> findByDelFlagAndTenantId(String delFlag, String tenantId);

}
