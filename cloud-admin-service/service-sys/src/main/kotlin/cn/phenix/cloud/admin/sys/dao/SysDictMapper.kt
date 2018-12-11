package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysDict
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * 字典管理
 * @author xiaobin
 * @create 2017-10-09 下午4:59
 */
interface SysDictMapper : GenericJpaRepository<SysDict, String> {

    @Modifying
    @Query("UPDATE SysDict SET sort = :sort WHERE id = :id")
    fun updateSort(@Param("sort") sort: Int?, @Param("id") id: String)

    @Modifying
    @Query("UPDATE SysDict SET sort =:sort")
    fun updateAllSort(@Param("sort") sort: Int?)

    fun findByCodeAndDelFlag(code: String, delFlag: String): List<SysDict>
}
