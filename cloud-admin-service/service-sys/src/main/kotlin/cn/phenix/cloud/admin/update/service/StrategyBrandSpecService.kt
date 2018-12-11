package cn.phenix.cloud.admin.update.service


import cn.phenix.cloud.admin.update.dao.StrategyBrandSpecMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.model.upgrade.StrategyBrandSpec
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 2018年1月3日
 */
@Service
@Transactional(readOnly = true)
class StrategyBrandSpecService : CoreService<StrategyBrandSpecMapper, StrategyBrandSpec>() {

    fun findByDelFlag(delFlag: String): List<StrategyBrandSpec> {
        return dao.findByDelFlag(delFlag)
    }
    fun findByUpgradeStrategyId(id: String): List<StrategyBrandSpec> {
        return dao.findByUpgradeStrategyId(id)
    }
}
