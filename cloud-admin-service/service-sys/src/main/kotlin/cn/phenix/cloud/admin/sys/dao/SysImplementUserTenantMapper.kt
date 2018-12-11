package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysImplementUserTenant
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * Created by mgm
 */
interface SysImplementUserTenantMapper : GenericJpaRepository<SysImplementUserTenant, String> {

    fun findByUserIdAndDelFlag(userId: String, delFlag: String): List<SysImplementUserTenant>

    @Modifying
    @Query("update SysImplementUserTenant set delFlag='1' where userId=:userId")
    fun updateDelFlag(@Param("userId") userId: String)

    @Modifying
    @Query("update SysImplementUserTenant set delFlag='1' where userId=:userId and tenantId=:tenantId")
    fun updateDelFlag(@Param("userId") userId: String, @Param("tenantId") tenantId: String)

    fun findByUserIdAndTenantIdAndDelFlag(userId: String, tenantId: String, delFlag: String): List<SysImplementUserTenant>
}
