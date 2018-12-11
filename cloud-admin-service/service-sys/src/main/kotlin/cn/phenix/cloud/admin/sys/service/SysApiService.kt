package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysApiMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.model.sys.SysApi
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 应用管理
 *
 * @author xiaobin
 * @create 2017-10-09 下午2:54
 */
@Service
@Transactional(readOnly = true)
class SysApiService : CoreService<SysApiMapper, SysApi>(){
    fun findByDelFlagAndAppId(appId: String): SysApi {
        return dao.findByDelFlagAndAppId(SysApi.DEL_FLAG_NORMAL, appId)
    }
}
