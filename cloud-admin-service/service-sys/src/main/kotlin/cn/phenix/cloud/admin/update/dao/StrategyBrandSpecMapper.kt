package cn.phenix.cloud.admin.update.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.upgrade.StrategyBrandSpec
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


/**
 * 2018年1月3日
 */
interface StrategyBrandSpecMapper : GenericJpaRepository<StrategyBrandSpec, String> {
    fun findByDelFlag(delFlag: String): List<StrategyBrandSpec>
    fun findByUpgradeStrategyId(id: String): List<StrategyBrandSpec>

    @Modifying
    @Query("update StrategyBrandSpec set delFlag ='1' where upgradeStrategyId = :upgradeStrategyId")
    fun updateFlagByUpgradeStrategyId(@Param("upgradeStrategyId") upgradeStrategyId: String?)

    fun deleteByUpgradeStrategyId(upgradeStrategyId: String?)
}

