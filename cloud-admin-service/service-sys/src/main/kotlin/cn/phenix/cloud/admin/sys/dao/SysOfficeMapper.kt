package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysOffice

/**
 * @author xiaobin
 * @create 2017-09-25 上午10:57
 */
interface SysOfficeMapper : GenericJpaRepository<SysOffice, String> {

    /**
     * 找到所有子节点
     * @return
     */
    fun findByDelFlagAndParentIdsLike(delFlag: String, parentIds: String): List<SysOffice>

}
