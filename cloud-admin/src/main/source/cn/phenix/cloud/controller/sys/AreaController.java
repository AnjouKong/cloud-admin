package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysAreaService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.model.sys.SysArea;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地区管理
 */
@Controller
@RequestMapping("platform/sys/area")
@RequiresPermissions("sys.manager.unit")
public class AreaController extends BaseController {
    @Autowired
    private SysAreaService sysAreaService;


    @GetMapping("/treeArea")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("地区树加载")
    public List<Map<String, Object>> treeTenancy(@Param("pid") String pid) {
        if(Strings.isBlank(pid)) pid="0";
        List<SysArea> list = sysAreaService.findAreaListByParentId(pid);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (SysArea area : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", area.getId());
            obj.put("text", area.getName());
            obj.put("children", true);
            tree.add(obj);
        }
        return tree;
    }
    @GetMapping("/strategyTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("策略树加载")
    public List<Map<String, Object>> strategyTree(@Param("pid") String pid) {
        if(Strings.isBlank(pid)) pid="1";
        List<SysArea> list = sysAreaService.findProvenceByPid(pid);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (SysArea area : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", area.getId());
            obj.put("text", area.getName());
            obj.put("areaCode", area.getAreaCode());
            obj.put("children", true);
            tree.add(obj);
        }
        return tree;
    }


}
