package cn.phenix.cloud.admin.tenant.renter.service;

import cn.phenix.cloud.admin.tenant.renter.dao.TenantLanguageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.tenant.renter.TenantLanguage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class TenantLanguageService extends CoreService<TenantLanguageMapper, TenantLanguage> {


    public List<TenantLanguage> findByTenantId(String tenantId) {
        return dao.findByTenantId(tenantId);
    }

    public void deleteByTenantId(String tenantId) {
        dao.deleteAll(dao.findByTenantId(tenantId));
    }
}
