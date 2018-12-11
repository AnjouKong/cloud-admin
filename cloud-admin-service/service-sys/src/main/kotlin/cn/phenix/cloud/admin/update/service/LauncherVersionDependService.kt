package cn.phenix.cloud.admin.update.service


import cn.phenix.cloud.admin.update.dao.LauncherVersionDependMapper
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.model.upgrade.LauncherVersionDepend
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 2018年1月31日
 */
@Service
@Transactional(readOnly = true)
class LauncherVersionDependService : CoreService<LauncherVersionDependMapper, LauncherVersionDepend>() {

    @Transactional
    fun deleteByVersionId(versionId: String) {
       dao.deleteByVersionId(versionId)
    }

    fun findByVersionId(versionId: String): List<LauncherVersionDepend> {
        return dao.findByVersionId(versionId)
    }

}
