package cn.phenix.cloud.admin.update.service


import cn.phenix.cloud.admin.update.dao.StrategyBrandSpecMapper
import cn.phenix.cloud.admin.update.dao.StrategyCityMapper
import cn.phenix.cloud.admin.update.dao.StrategyDeviceGroupMapper
import cn.phenix.cloud.admin.update.dao.UpgradeStrategyMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.DataTableParameter
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.cloud.jpa.data.PageTable
import cn.phenix.common.utils.StringUtils
import cn.phenix.model.upgrade.StrategyBrandSpec
import cn.phenix.model.upgrade.StrategyCity
import cn.phenix.model.upgrade.StrategyDeviceGroup
import cn.phenix.model.upgrade.UpgradeStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 2018年1月3日
 */
@Service
@Transactional(readOnly = true)
class UpgradeStrategyService : CoreService<UpgradeStrategyMapper, UpgradeStrategy>() {
    @Autowired
    private val upgradeStrategyMapper: UpgradeStrategyMapper? = null
    @Autowired
    private val strategyBrandSpecMapper: StrategyBrandSpecMapper? = null
    @Autowired
    private val strategyCityMapper: StrategyCityMapper? = null
    @Autowired
    private val strategyDeviceGroupMapper: StrategyDeviceGroupMapper? = null


    fun findPage(parameter: DataTableParameter, launcher: UpgradeStrategy,versionId: String,launcherId: String): PageTable<UpgradeStrategy> {

        val pageRequest = parameter.pageRequest
        val finder = Finder.create("from UpgradeStrategy a where a.delFlag=:delFlag ")
                .setParam("delFlag",UpgradeStrategy.DEL_FLAG_NORMAL)
        finder.search("a.name",launcher.name,Finder.SearchType.LIKE)
        if(launcherId =="#"){
            finder.append("and a.launcherVersion.id in(select id from LauncherVersion where launcher.id=:versionId)").setParam("versionId",versionId)
        }else if(StringUtils.isNotEmpty(launcherId)){
            finder.search("a.launcherVersion.id",versionId,Finder.SearchType.EQ)
        }
        val page = upgradeStrategyMapper!!.find(finder, pageRequest)
        return PageTable(page)
    }

    fun findByDelFlag(delFlag: String): List<UpgradeStrategy> {
        return dao.findByDelFlag(delFlag)
    }
    fun findByLauncherVersionIdIn(ids: List<String>): List<UpgradeStrategy> {
        return dao.findByDelFlagAndLauncherVersionIdIn("0",ids)
    }

    @Transactional
    fun saveCascade(upgradeStrategy: UpgradeStrategy, cityList: List<StrategyCity>, specList: List<StrategyBrandSpec>,groupList: List<StrategyDeviceGroup>) {
        dao.save(upgradeStrategy)
        //商户相关
        if(groupList.isNotEmpty() && groupList[0].tenantId != ("clearAll")){
            strategyDeviceGroupMapper!!.deleteByUpgradeStrategyId(upgradeStrategy.id)
            for(strategyDeviceGroup in groupList){
                strategyDeviceGroupMapper!!.save(strategyDeviceGroup)
            }
        }else if(groupList.isNotEmpty() && groupList[0].tenantId == ("clearAll")){
            strategyDeviceGroupMapper!!.deleteByUpgradeStrategyId(upgradeStrategy.id)
        }
        //城市相关
        if(cityList.isNotEmpty() && cityList[0].provinceId != ("clearAll")){
            strategyCityMapper!!.deleteByUpgradeStrategyId(upgradeStrategy.id)
            for(strategyCity in cityList){
                strategyCityMapper!!.save(strategyCity)
            }
        }else if(cityList.isNotEmpty() && cityList[0].provinceId == ("clearAll")){
            strategyCityMapper!!.deleteByUpgradeStrategyId(upgradeStrategy.id)
        }
        //品牌相关
        if(specList.isNotEmpty() && specList[0].brandId != ("clearAll")) {
            strategyBrandSpecMapper!!.deleteByUpgradeStrategyId(upgradeStrategy.id)
            for (strategyBrandSpec in specList) {
                strategyBrandSpecMapper!!.save(strategyBrandSpec)
            }
        }else if(specList.isNotEmpty() && specList[0].brandId == ("clearAll")) {
            strategyBrandSpecMapper!!.deleteByUpgradeStrategyId(upgradeStrategy.id)
        }
    }

    @Transactional
    fun deleteCascade(ids: List<String>) {
        for(id in ids){
            dao.delById(id)
            strategyBrandSpecMapper!!.updateFlagByUpgradeStrategyId(id)
            strategyCityMapper!!.updateFlagByUpgradeStrategyId(id)
            strategyDeviceGroupMapper!!.updateFlagByUpgradeStrategyId(id)
        }
    }
}
