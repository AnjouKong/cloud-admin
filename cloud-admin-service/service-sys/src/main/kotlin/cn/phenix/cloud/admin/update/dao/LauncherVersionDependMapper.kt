package cn.phenix.cloud.admin.update.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.upgrade.LauncherVersionDepend


/**
 * 2018年1月31日
 */
interface LauncherVersionDependMapper : GenericJpaRepository<LauncherVersionDepend, String> {

    fun deleteByVersionId(versionId: String)
    fun findByVersionId(versionId: String): List<LauncherVersionDepend>
}

