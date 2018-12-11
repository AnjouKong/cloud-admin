package cn.phenix.cloud.admin.update.service


import cn.phenix.cloud.admin.update.dao.StrategyDeviceGroupMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.model.upgrade.StrategyDeviceGroup
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 2018年2月6日
 */
@Service
@Transactional(readOnly = true)
class StrategyDeviceGroupService : CoreService<StrategyDeviceGroupMapper, StrategyDeviceGroup>() {

    fun findByDelFlag(delFlag: String): List<StrategyDeviceGroup> {
        return dao.findByDelFlag(delFlag)
    }
    fun findByUpgradeStrategyId(id: String): List<StrategyDeviceGroup> {
        return dao.findByUpgradeStrategyId(id)
    }

}
