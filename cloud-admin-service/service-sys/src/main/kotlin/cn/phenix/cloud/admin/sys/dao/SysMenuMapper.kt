package cn.phenix.cloud.admin.sys.dao

import cn.phenix.cloud.jpa.GenericJpaRepository
import cn.phenix.model.sys.SysMenu
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author xiaobin
 * @create 2017-09-23 下午3:50
 */
interface SysMenuMapper : GenericJpaRepository<SysMenu, String> {

    //---------------------end 动态sql--------------------

    fun findByDelFlagAndHrefIn(delFlag: String,hrefList: List<String>, sort: Sort):List<SysMenu>

    fun findByDelFlagAndParentIdsLike(delFlag: String, parentIds: String): List<SysMenu>

    override fun delete(menu: SysMenu)

    @Modifying
    @Query("UPDATE SysMenu SET sort = :sort WHERE id = :id")
    fun updateSort(@Param("sort") sort: Int?, @Param("id") id: String)

    @Modifying
    @Query("UPDATE SysMenu SET sort =:sort")
    fun updateAllSort(@Param("sort") sort: Int?)

    companion object {

        //---------------------start 动态sql--------------------
        //根据用户查询对应的链接
        val findMenuByUserId = "findMenuByUserId"

        //path、type、parent
        val findAllMenu = "findAllMenu"

        val findMenuByUserIdAndType = "findMenuByUserIdAndType"


        /**
         * 根据角色查询菜单
         */
        val findMenusAndButtonsByRole = "findMenusAndButtonsByRole"
    }
}
