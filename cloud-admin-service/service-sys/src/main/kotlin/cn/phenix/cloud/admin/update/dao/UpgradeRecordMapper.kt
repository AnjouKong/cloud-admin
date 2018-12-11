package cn.phenix.cloud.admin.update.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.upgrade.UpgradeRecord


/**
 * 2018年1月3日
 */
interface UpgradeRecordMapper : GenericJpaRepository<UpgradeRecord, String> {

}

