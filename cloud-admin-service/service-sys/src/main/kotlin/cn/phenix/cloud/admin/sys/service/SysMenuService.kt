package cn.phenix.cloud.admin.sys.service

import cn.phenix.cloud.admin.sys.dao.SysMenuMapper
import cn.phenix.cloud.core.utils.Strings
import cn.phenix.cloud.jpa.CoreService
import cn.phenix.cloud.jpa.data.Finder
import cn.phenix.cloud.jpa.dynamic.Parameter
import cn.phenix.model.sys.SysMenu
import cn.phenix.model.sys.SysUser
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author xiaobin
 * @create 2017-09-25 上午11:04
 */
@Service
@Transactional(readOnly = true)
class SysMenuService : CoreService<SysMenuMapper, SysMenu>() {

    @Autowired
    private val sysUserService: SysUserService? = null

    override fun get(id: String): SysMenu {
        return dao.findById(id).orElse(null)!!
    }

    @Transactional
    fun delete(id: String) = dao.deleteById(id)

    @Transactional
    fun update(menu: SysMenu) = dao.save(menu)!!

    @Transactional
    override fun save(menu: SysMenu): SysMenu {
        menu.parent = this.get(menu.parent.id)
        val oldParentIds = menu.parentIds // 获取修改前的parentIds，用于更新子节点的parentIds
        var parentIds = ""
        if (menu.parent != null) {
            parentIds = menu.parent.parentIds + menu.parent.id + ","
        } else {
            parentIds = menu.id!! + ","
            menu.parent = SysMenu(SysMenu.ROOT_PARENT_ID)
        }
        menu.parentIds = parentIds

        // 更新子节点 parentIds
        val list = dao.findByDelFlagAndParentIdsLike(SysMenu.DEL_FLAG_NORMAL, "%," + menu.id + ",%")
        for (e in list) {
            e.parentIds = e.parentIds.replace(oldParentIds, menu.parentIds)
        }
        return dao.save(menu)
    }

    fun findMenusAndButtonsByRole(roleId: String?, type: String?): List<SysMenu> {
        return dao.findByNamedQuery(SysMenuMapper.findMenusAndButtonsByRole,
                Parameter.create().insert("roleId", roleId).insert("delFlag", SysMenu.DEL_FLAG_NORMAL).insert("type", type))
    }

    fun findByPathAndType(path: String?, type: String?): List<SysMenu> {
        return dao.findByNamedQuery(SysMenuMapper.findAllMenu, Parameter.create().insert("path", path).insert("type", type))
    }

    fun findByHrefLoadParentId(hrefList: List<String>): String? {
        var menuList: List<SysMenu>? = dao.findByDelFlagAndHrefIn(SysMenu.DEL_FLAG_NORMAL, hrefList, Sort.by("sort"))
        if (menuList!!.isNotEmpty()) {
            var menu = menuList[0]
            menu = dao.findById(menu.parentId).orElse(null)
            if (menu != null) {
                return menu.id
            }
        }
        return null
    }

    fun findByHrefLoadRootParent(hrefList: List<String>): SysMenu? {
        var menuList: List<SysMenu>? = dao.findByDelFlagAndHrefIn(SysMenu.DEL_FLAG_NORMAL, hrefList, Sort.by("sort"))
        if (menuList!!.isNotEmpty()) {
            var menu = menuList[0]
            if (SysMenu.ROOT_PARENT_ID == menu.parentId) {
                return menu
            } else {
                START@ while (true) {
                    menu = menu.parent
                    if (menu == null)
                        break@START
                    else if (SysMenu.ROOT_PARENT_ID == menu.parentId)
                        return menu
                }
            }
        }
        return null
    }

    /**
     * 控制面板
     *
     * @return
     */
    fun findMenuList(parentId: String?): List<SysMenu> {
        val finder = Finder.create("select m from SysMenu m left join SysMenu parent on m.parent.id=parent.id where m.delFlag=:delFlag and parent.delFlag=:delFlag")
                .setParam("delFlag", SysMenu.DEL_FLAG_NORMAL)
        finder.search("m.parent.id", parentId, Finder.SearchType.EQ)
        finder.search("m.isShow", true, Finder.SearchType.EQ)
        finder.append("order by m.sort")
        return dao.find(finder)
    }

    fun findByUserId(userId: String?, parentId: String?, isShow: Boolean?): List<SysMenu> {
        val sysUser = sysUserService!!.get(userId)
        return findMenusByUser(sysUser, parentId, isShow)
    }

    @JvmOverloads
    fun findMenusByUser(user: SysUser?, parentId: String?, isShow: Boolean? = null): List<SysMenu> {
        val menuList: List<SysMenu>
        val m = SysMenu()
        if (user!!.isAdmin) {
            menuList = dao.findByNamedQuery(SysMenuMapper.findAllMenu, Parameter.create().insert("isShow", isShow)
                    .insert("parentId", parentId))
        } else {
            m.userId = user.id
            m.parent = SysMenu(parentId)
            m.isShow = isShow
            menuList = dao.findByNamedQuery(SysMenuMapper.findMenuByUserId,
                    Parameter.create().insertExcludeDel().insert("id", user.id).insert("isShow", isShow)
                            .insert("parentId", parentId))
        }
        return menuList
    }

    @Transactional
    fun sortDo(ids: String?) {
        val menuIds = StringUtils.split(ids, ",")
        dao.updateAllSort(0)
        menuIds
                .filterNot { Strings.isBlank(it) }
                .forEachIndexed { i, s -> dao.updateSort(i, s) }
    }
}
