package cn.phenix.cloud.admin.update.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.upgrade.UpgradeStrategy
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


/**
 * 2018年1月3日
 */
interface UpgradeStrategyMapper : GenericJpaRepository<UpgradeStrategy, String> {
    fun findByDelFlag(delFlag: String): List<UpgradeStrategy>
    fun findByDelFlagAndLauncherVersionIdIn(delFlag: String,ids: List<String>): List<UpgradeStrategy>

    @Modifying
    @Query("update UpgradeStrategy set delFlag ='1' where id=:id")
    fun delById(@Param("id")id: String)
}

