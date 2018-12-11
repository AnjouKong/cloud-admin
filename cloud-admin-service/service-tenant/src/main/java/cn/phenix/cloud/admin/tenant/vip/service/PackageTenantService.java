package cn.phenix.cloud.admin.tenant.vip.service;

import cn.phenix.cloud.admin.tenant.vip.dao.PackageTenantMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.tenant.vip.PackageTenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author socilents
 * Create in 2018/5/25 14:42
 **/
@Service
@Transactional(readOnly = true)
public class PackageTenantService extends CoreService<PackageTenantMapper,PackageTenant>{
    @Autowired
    private PackageTenantMapper packageTenantMapper;
    @Transactional
    public void deleteByTenantId(String tenantId) {
        packageTenantMapper.deleteByTenantId(tenantId);
    }

    public PackageTenant getPTinfoByTenantId(String tenantId){
        return packageTenantMapper.getPTinfoByTenantId(tenantId);
    }

    @Transactional
    public void deleteByPackageIdAndTenantId(String packageId, String tenantId) {
        packageTenantMapper.deleteByPackageIdAndTenantId(packageId,tenantId);
    }
}
