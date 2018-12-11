package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysArea

/**
 * 地区管理
 */
interface SysAreaMapper : GenericJpaRepository<SysArea, String> {
    fun findNameByIdIn(ids: List<String>): List<String>
}