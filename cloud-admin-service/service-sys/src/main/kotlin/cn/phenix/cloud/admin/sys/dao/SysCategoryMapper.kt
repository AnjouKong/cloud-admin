package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysCategory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * 分类管理
 */
interface SysCategoryMapper : GenericJpaRepository<SysCategory, String> {

    @Modifying
    @Query("UPDATE SysCategory SET sort = :sort WHERE id = :id")
    fun updateSort(@Param("sort") sort: Int?, @Param("id") id: String)

    @Modifying
    @Query("UPDATE SysCategory SET sort =:sort WHERE modelName = :modelName")
    fun updateAllSort(@Param("sort") sort: Int?, @Param("modelName") modelName: String)

}