package cn.phenix.cloud.admin.update.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.upgrade.StrategyDeviceGroup
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


/**
 * 2018年2月6日
 */
interface StrategyDeviceGroupMapper : GenericJpaRepository<StrategyDeviceGroup, String> {
    fun findByDelFlag(delFlag: String): List<StrategyDeviceGroup>
    fun findByUpgradeStrategyId(id: String): List<StrategyDeviceGroup>

    @Modifying
    @Query("update StrategyDeviceGroup set delFlag ='1' where upgradeStrategyId = :upgradeStrategyId")
    fun updateFlagByUpgradeStrategyId(@Param("upgradeStrategyId") upgradeStrategyId: String?)

    fun deleteByUpgradeStrategyId(upgradeStrategyId: String?)
}

