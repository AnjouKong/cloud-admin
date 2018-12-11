package cn.phenix.cloud.admin.update.service


import cn.phenix.cloud.admin.update.dao.StrategyCityMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.model.upgrade.StrategyCity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 2018年1月3日
 */
@Service
@Transactional(readOnly = true)
class StrategyCityService : CoreService<StrategyCityMapper, StrategyCity>() {

    fun findByDelFlag(delFlag: String): List<StrategyCity> {
        return dao.findByDelFlag(delFlag)
    }
    fun findByUpgradeStrategyId(id: String): List<StrategyCity> {
        return dao.findByUpgradeStrategyId(id)
    }

}
