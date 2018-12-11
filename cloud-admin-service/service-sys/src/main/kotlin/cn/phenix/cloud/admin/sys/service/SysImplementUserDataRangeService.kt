package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysImplementUserDataRangeMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.model.sys.SysImplementUserDataRange
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 *
 */
@Service
@Transactional(readOnly = true)
class SysImplementUserDataRangeService : CoreService<SysImplementUserDataRangeMapper, SysImplementUserDataRange>() {

    @Autowired
    private val sysImplementUserDataRangeMapper: SysImplementUserDataRangeMapper? = null

    fun findByDelFlagAndUserId(delFlag: String, userId: String): SysImplementUserDataRange {
        return sysImplementUserDataRangeMapper!!.findByDelFlagAndUserId(delFlag, userId)
    }
}
