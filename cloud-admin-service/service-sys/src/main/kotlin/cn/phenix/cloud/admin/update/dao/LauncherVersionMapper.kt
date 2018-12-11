package cn.phenix.cloud.admin.update.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.upgrade.LauncherVersion
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


/**
 * 2018年1月3日17:49:05
 */
interface LauncherVersionMapper : GenericJpaRepository<LauncherVersion, String> {


    fun findByStrategyIdIn(ids: Array<String>): List<LauncherVersion>

    @Modifying
    @Query("update LauncherVersion set delFlag='1' where launcherId=:launcherId ")
    fun updateByLauncherId(@Param("launcherId") launcherId: String)

    fun findByLauncherIdIn(ids: Array<String>): List<LauncherVersion>

    fun findByLauncherIdAndDelFlag(launcherId: String, delFlag: String): List<LauncherVersion>
    fun findIdByLauncherIdIn(ids: Array<String>): List<LauncherVersion>
    fun findByVersionAndDelFlagAndLauncherId(version: String, delFlag: String, launcherId: String): List<LauncherVersion>
}

