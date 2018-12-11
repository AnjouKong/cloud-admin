package cn.phenix.cloud.admin.tenant.renter.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.renter.TenantOffice;

public interface TenantOfficeMapper extends GenericJpaRepository<TenantOffice,String> {
    String find4Add = "find4Add";
}
