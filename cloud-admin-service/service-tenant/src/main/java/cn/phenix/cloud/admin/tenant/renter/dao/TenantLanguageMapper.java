package cn.phenix.cloud.admin.tenant.renter.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.renter.TenantLanguage;

import java.util.List;


/**
 * Created by fxq
 */

public interface TenantLanguageMapper extends GenericJpaRepository<TenantLanguage,String> {

    List<TenantLanguage> findByTenantId(String tenantId);
}
