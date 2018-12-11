package cn.phenix.cloud.base.config.data;

import cn.phenix.cloud.admin.sys.dao.SysMenuMapper;
import cn.phenix.model.sys.SysMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaobin
 * @create 2017-09-26 下午1:35
 **/
@Component
public class DataInitConfig implements CommandLineRunner {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public void run(String... args) throws Exception {
        SysMenu sysMenu = sysMenuMapper.findById("1").orElse(null);
        if (sysMenu != null && StringUtils.isNotBlank(sysMenu.getId())) {
            return;
        }
        //初始化菜单
        List<SysMenu> menuList = new ArrayList<>();
        SysMenu menu = new SysMenu();
        menu.setId("1");
        menu.setPath("0001");
        menu.setName("系统");
        menu.setRemarks("系统");
        menu.setIcon("");
        menu.setSort(0);
        menu.setHref("");
        menu.setTarget("");
        menu.setIsShow(true);
        menu.setHasChildren(true);
        menu.setParentId("0");
        menu.setType("menu");
        menu.setPermission("sys");
        sysMenuMapper.save(menu);
        String parentId = menu.getId();
        menu = new SysMenu();

        menu.setPath("00010001");
        menu.setName("组织权限");
        menu.setRemarks("组织权限");
        menu.setAliasName("Role_Manager");
        menu.setIcon("ti-settings");
        menu.setSort(0);
        menu.setHref("");
        menu.setTarget("");
        menu.setIsShow(true);
        menu.setHasChildren(true);
        menu.setParentId(parentId);
        menu.setType("menu");
        menu.setPermission("sys.manager");
        sysMenuMapper.save(menu);
        String m1_Id = menu.getId();

        menu = new SysMenu();
        menu.setPath("00010002");
        menu.setName("系统管理");
        menu.setRemarks("系统管理");
        menu.setAliasName("Manager");
        menu.setIcon("ti-settings");
        menu.setSort(0);
        menu.setHref("");
        menu.setTarget("");
        menu.setIsShow(true);
        menu.setHasChildren(true);
        menu.setParentId(parentId);
        menu.setType("menu");
        menu.setPermission("sys.manager");
        sysMenuMapper.save(menu);
        String m2_Id = menu.getId();

        menu = new SysMenu();
        menu.setPath("000100010001");
        menu.setName("单位管理");
        menu.setAliasName("Unit");
        menu.setSort(0);
        menu.setHref("/platform/sys/office");
        menu.setTarget("data-pjax");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.unit");
        menu.setParentId(m1_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String m2Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100010001");
        menu.setName("添加单位");
        menu.setAliasName("Add");
        menu.setSort(0);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.unit.add");
        menu.setParentId(m2Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m21Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100010002");
        menu.setName("修改单位");
        menu.setAliasName("Edit");
        menu.setSort(0);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.unit.edit");
        menu.setParentId(m2Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m22Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100010003");
        menu.setName("删除单位");
        menu.setAliasName("Delete");
        menu.setSort(0);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.unit.delete");
        menu.setParentId(m2Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m23Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("000100010002");
        menu.setName("用户管理");
        menu.setAliasName("User");
        menu.setSort(0);
        menu.setHref("/platform/sys/user");
        menu.setTarget("data-pjax");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.user");
        menu.setHasChildren(false);
        menu.setParentId(m1_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String m3Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100020001");
        menu.setName("添加用户");
        menu.setAliasName("Add");
        menu.setSort(0);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.user.add");
        menu.setParentId(m3Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m31Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100020002");
        menu.setName("修改用户");
        menu.setAliasName("Edit");
        menu.setSort(1);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.user.edit");
        menu.setParentId(m3Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m32Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100020003");
        menu.setName("删除用户");
        menu.setAliasName("Delete");
        menu.setSort(2);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.user.delete");
        menu.setParentId(m3Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m33Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("000100010003");
        menu.setName("角色管理");
        menu.setAliasName("Role");
        menu.setSort(0);
        menu.setHref("/platform/sys/role");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.role");
        menu.setTarget("data-pjax");
        menu.setParentId(m1_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String m4Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100030001");
        menu.setName("添加角色");
        menu.setAliasName("Add");
        menu.setSort(1);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.role.add");
        menu.setParentId(m4Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m41Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100030002");
        menu.setName("修改角色");
        menu.setAliasName("Edit");
        menu.setSort(2);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.role.edit");
        menu.setParentId(m4Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m42Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100030003");
        menu.setName("删除角色");
        menu.setAliasName("Delete");
        menu.setSort(3);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.role.delete");
        menu.setParentId(m4Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m43Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100030004");
        menu.setName("分配菜单");
        menu.setAliasName("SetMenu");
        menu.setSort(4);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.role.menu");
        menu.setParentId(m4Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m44Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100030005");
        menu.setName("分配用户");
        menu.setAliasName("SetUser");
        menu.setSort(5);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.role.user");
        menu.setParentId(m4Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m45Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("000100010004");
        menu.setName("菜单管理");
        menu.setAliasName("Menu");
        menu.setSort(0);
        menu.setHref("/platform/sys/menu");
        menu.setTarget("data-pjax");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.menu");
        menu.setParentId(m1_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String m5Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100040001");
        menu.setName("添加菜单");
        menu.setAliasName("Add");
        menu.setSort(1);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.menu.add");
        menu.setParentId(m5Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m51Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100040002");
        menu.setName("修改菜单");
        menu.setAliasName("Edit");
        menu.setSort(2);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.menu.edit");
        menu.setParentId(m5Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m52Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000100040003");
        menu.setName("删除菜单");
        menu.setAliasName("Delete");
        menu.setSort(3);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.menu.delete");
        menu.setParentId(m5Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m53Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("000100020001");
        menu.setName("系统参数");
        menu.setAliasName("Param");
        menu.setSort(0);
        menu.setHref("/platform/sys/conf");
        menu.setTarget("data-pjax");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.conf");
        menu.setParentId(m2_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String m6Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200050001");
        menu.setName("添加参数");
        menu.setAliasName("Add");
        menu.setSort(1);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.conf.add");
        menu.setParentId(m6Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m61Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200050002");
        menu.setName("修改参数");
        menu.setAliasName("Edit");
        menu.setSort(2);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.conf.edit");
        menu.setParentId(m6Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m62Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200050003");
        menu.setName("删除参数");
        menu.setAliasName("Delete");
        menu.setSort(3);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.conf.delete");
        menu.setParentId(m6Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m63Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("000100020006");
        menu.setName("日志管理");
        menu.setAliasName("Log");
        menu.setSort(0);
        menu.setHref("/platform/sys/log");
        menu.setTarget("data-pjax");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.log");
        menu.setParentId(m2_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String m7Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200060001");
        menu.setName("清除日志");
        menu.setAliasName("Delete");
        menu.setSort(1);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.log.delete");
        menu.setParentId(m7Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m71Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("000100020007");
        menu.setName("定时任务");
        menu.setAliasName("Task");
        menu.setSort(0);
        menu.setHref("/platform/sys/task");
        menu.setTarget("data-pjax");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.task");
        menu.setParentId(m2_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String m8Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200070001");
        menu.setName("添加任务");
        menu.setAliasName("Add");
        menu.setSort(1);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.task.add");
        menu.setParentId(m8Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m81Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200070002");
        menu.setName("修改任务");
        menu.setAliasName("Edit");
        menu.setSort(2);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.task.edit");
        menu.setParentId(m8Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m82Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200070003");
        menu.setName("删除任务");
        menu.setAliasName("Delete");
        menu.setSort(3);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.task.delete");
        menu.setParentId(m8Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m83Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("000100020008");
        menu.setName("自定义路由");
        menu.setAliasName("Route");
        menu.setSort(0);
        menu.setHref("/platform/sys/route");
        menu.setTarget("data-pjax");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.route");
        menu.setParentId(m2_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String m9Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200080001");
        menu.setName("添加路由");
        menu.setAliasName("Add");
        menu.setSort(1);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.route.add");
        menu.setParentId(m9Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m91Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200080002");
        menu.setName("修改路由");
        menu.setAliasName("Edit");
        menu.setSort(2);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.route.edit");
        menu.setParentId(m9Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m92Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200080003");
        menu.setName("删除路由");
        menu.setAliasName("Delete");
        menu.setSort(3);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.route.delete");
        menu.setParentId(m9Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String m93Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000m2_Id0009");
        menu.setName("应用管理");
        menu.setAliasName("App");
        menu.setSort(0);
        menu.setHref("/platform/sys/api");
        menu.setTarget("data-pjax");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.api");
        menu.setParentId(m2_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String mm1_Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200090001");
        menu.setName("添加应用");
        menu.setAliasName("Add");
        menu.setSort(1);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.api.add");
        menu.setParentId(m2_Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String mm2Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200090002");
        menu.setName("修改应用");
        menu.setAliasName("Edit");
        menu.setSort(2);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.api.edit");
        menu.setParentId(m2_Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String mm3Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200090003");
        menu.setName("删除应用");
        menu.setAliasName("Delete");
        menu.setSort(3);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.api.delete");
        menu.setParentId(m2_Id);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String mm4Id = menu.getId();
        menu = new SysMenu();

        menu.setPath("000100020010");
        menu.setName("数据字典");
        menu.setAliasName("Dict");
        menu.setSort(0);
        menu.setHref("/platform/sys/dict");
        menu.setTarget("data-pjax");
        menu.setIsShow(true);
        menu.setPermission("sys.manager.dict");
        menu.setParentId(m2_Id);
        menu.setType("menu");
        sysMenuMapper.save(menu);
        String did = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200100001");
        menu.setName("添加字典");
        menu.setAliasName("Add");
        menu.setSort(1);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.dict.add");
        menu.setParentId(did);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String did1 = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200100002");
        menu.setName("修改字典");
        menu.setAliasName("Edit");
        menu.setSort(2);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.dict.edit");
        menu.setParentId(did);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String did2 = menu.getId();
        menu = new SysMenu();

        menu.setPath("0001000200100003");
        menu.setName("删除字典");
        menu.setAliasName("Delete");
        menu.setSort(3);
        menu.setIsShow(false);
        menu.setPermission("sys.manager.dict.delete");
        menu.setParentId(did);
        menu.setType("data");
        sysMenuMapper.save(menu);
        String did3 = menu.getId();

    }
}
