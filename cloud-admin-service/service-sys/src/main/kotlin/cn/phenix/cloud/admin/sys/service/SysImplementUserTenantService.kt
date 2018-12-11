package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysImplementUserTenantMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.model.sys.SysImplementUserTenant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 *
 */
@Service
@Transactional(readOnly = true)
class SysImplementUserTenantService : CoreService<SysImplementUserTenantMapper, SysImplementUserTenant>() {

    @Autowired
    private val sysImplementUserTenantMapper: SysImplementUserTenantMapper? = null


    fun findByUserIdAndTenantId(userId: String?, tenantId: String?): List<SysImplementUserTenant> {

        return sysImplementUserTenantMapper!!.findByUserIdAndTenantIdAndDelFlag(userId!!, tenantId!!, SysImplementUserTenant.DEL_FLAG_NORMAL)
    }

    @Transactional
    fun updateDelFlag(userId: String) {
        sysImplementUserTenantMapper!!.updateDelFlag(userId)
    }

    @Transactional
    fun updateDelFlag(userId: String, tenantId: String) {
        sysImplementUserTenantMapper!!.updateDelFlag(userId, tenantId)
    }

}
