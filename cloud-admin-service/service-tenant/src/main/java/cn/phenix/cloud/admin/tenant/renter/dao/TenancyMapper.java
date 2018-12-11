package cn.phenix.cloud.admin.tenant.renter.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.renter.Tenancy;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface TenancyMapper extends GenericJpaRepository<Tenancy,String> {

    Tenancy findByTenancyCodeAndDelFlag(String tenancyCode,String delFlag);
    List<Tenancy> findByDelFlag(String delFlag);

    List<Tenancy> findByIdIn(String[] tenantId);
}
