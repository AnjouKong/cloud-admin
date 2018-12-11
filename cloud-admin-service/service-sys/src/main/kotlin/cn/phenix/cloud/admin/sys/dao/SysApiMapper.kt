package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysApi

/**
 * 应用管理
 *
 * @author xiaobin
 * @create 2017-10-09 下午2:53
 */
interface SysApiMapper : GenericJpaRepository<SysApi, String>{
    fun findByDelFlagAndAppId(deL_FLAG_NORMAL: String, appId: String): SysApi
}
