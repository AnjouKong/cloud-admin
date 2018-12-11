package cn.phenix.cloud.admin.update.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.upgrade.StrategyCity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


/**
 * 2018年1月3日
 */
interface StrategyCityMapper : GenericJpaRepository<StrategyCity, String> {
    fun findByDelFlag(delFlag: String): List<StrategyCity>
    fun findByUpgradeStrategyId(id: String): List<StrategyCity>

    @Modifying
    @Query("update StrategyCity set delFlag ='1' where upgradeStrategyId = :upgradeStrategyId")
    fun updateFlagByUpgradeStrategyId(@Param("upgradeStrategyId") upgradeStrategyId: String?)

    fun deleteByUpgradeStrategyId(upgradeStrategyId: String?)
}

