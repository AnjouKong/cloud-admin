package cn.phenix.cloud.admin.tenant.vip.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.vip.PackageTenant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author socilents
 * Create in 2018/5/25 14:46
 **/
public interface PackageTenantMapper  extends GenericJpaRepository<PackageTenant,String> {

    @Modifying
    @Query("delete from PackageTenant where tenantId= :tenantId")
    void deleteByTenantId(@Param("tenantId")  String tenantId);


    @Query("select a from PackageTenant a where a.tenantId=:tenantId")
    PackageTenant getPTinfoByTenantId(@Param("tenantId") String tenantId);

    @Modifying
    @Query("delete from PackageTenant where tenantId= :tenantId and packageId=:packageId")
    void deleteByPackageIdAndTenantId(@Param("packageId")String packageId,@Param("tenantId") String tenantId);

}
