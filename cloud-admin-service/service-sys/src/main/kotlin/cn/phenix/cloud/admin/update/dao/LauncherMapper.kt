package cn.phenix.cloud.admin.update.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.upgrade.Launcher



/**
 * 2018年1月3日
 */
interface LauncherMapper : GenericJpaRepository<Launcher, String> {
    fun findByDelFlag(delFlag: String): List<Launcher>
    fun findByCodeAndDelFlag(code: String, delFlag: String): List<Launcher>
    fun findByDelFlagAndIdNotIn(delFlag: String, id: String): List<Launcher>
}

