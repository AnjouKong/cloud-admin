package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysImplementUserDataRange

/**
 * Created by mgm
 */
interface SysImplementUserDataRangeMapper : GenericJpaRepository<SysImplementUserDataRange, String> {

    fun findByDelFlagAndUserId(delFlag: String, userId: String): SysImplementUserDataRange
}
