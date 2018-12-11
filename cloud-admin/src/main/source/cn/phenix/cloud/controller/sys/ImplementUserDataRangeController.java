package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysImplementUserDataRangeService;
import cn.phenix.cloud.admin.sys.service.SysImplementUserTenantService;
import cn.phenix.cloud.admin.tenant.renter.service.TenancyService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.model.sys.SysImplementUserDataRange;
import cn.phenix.model.sys.SysImplementUserTenant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 实施管理
 **/
@Controller
@Api(tags = "实施管理")
@RequestMapping("platform/sys/implement")
public class ImplementUserDataRangeController extends BaseController {

    @Autowired
    private SysImplementUserDataRangeService sysImplementUserDataRangeService;

    @Autowired
    private SysImplementUserTenantService sysImplementUserTenantService;

    @Autowired
    private TenancyService tenancyService;

    @GetMapping("")
    @ApiOperation("首页跳转")
    @RequiresPermissions("sys.manager.unit")
    public String index() {
        return "sys/implement/index";
    }

    @GetMapping("add/{id}")
    @ApiOperation("添加页面跳转")
    @RequiresPermissions("sys.manager.unit")
    public String authorization(@PathVariable("id") String id, Model model) {
        SysImplementUserDataRange range = sysImplementUserDataRangeService.findByDelFlagAndUserId(SysImplementUserDataRange.DEL_FLAG_NORMAL, id);
        if (range == null) range = new SysImplementUserDataRange();
        model.addAttribute("obj", tenancyService.findTenancyList(id));
        model.addAttribute("range", range);
        model.addAttribute("userId", id);
        return "sys/implement/add";
    }

    @GetMapping("tenant/{id}")
    @ApiOperation("商户页面跳转")
    @RequiresPermissions("sys.manager.unit")
    public String tenant(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", tenancyService.findTenancyList(id));
        model.addAttribute("userId", id);
        return "sys/implement/tenant";
    }

    @GetMapping("tenantValidate/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("权限分配")
    public Result tenantValidate(@PathVariable("id") String id) {
        Result result=new Result();
        SysImplementUserDataRange range = sysImplementUserDataRangeService.findByDelFlagAndUserId(SysImplementUserDataRange.DEL_FLAG_NORMAL, id);
        if(range==null||!SysImplementUserDataRange.RANGE_ALLOT.equalsIgnoreCase(range.getRoleRange())){
            result.setMsg("当用户权限方式为'分配'时才需分配权限！");
            result.setCode(1);
        }
        return result;
    }

    @GetMapping("selectTenant")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择商户")
    public String selectTenant() {
        return "sys/implement/selectTenant";
    }

    @PostMapping("pushTenant")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result pushTenant(@Param("userId")String userId,@Param("tenantIds")String tenantIds) {
        for(String str:tenantIds.split(",")){
            if(sysImplementUserTenantService.findByUserIdAndTenantId(userId,str).size()>0) continue;
            SysImplementUserTenant s=new SysImplementUserTenant();
            s.setTenantId(str);
            s.setUserId(userId);
            sysImplementUserTenantService.save(s);
        }
        return Result.success();
    }

    @PostMapping("delete")
    @ResponseBody
    @ApiOperation("删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@Param("userId") String userId,@Param("tenantIds") String tenantId) {
        sysImplementUserTenantService.updateDelFlag(userId,tenantId);
        return Result.success();
    }


    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(SysImplementUserDataRange range) {
        if (sysImplementUserDataRangeService.get(range.getId()) != null)
            return Result.error("系統異常");
        SysImplementUserDataRange sysImplementUserDataRange = sysImplementUserDataRangeService.findByDelFlagAndUserId(SysImplementUserDataRange.DEL_FLAG_NORMAL, range.getUserId());
        if (sysImplementUserDataRange == null) {
            sysImplementUserDataRangeService.save(range);
        } else {
            if(range.getRoleRange()!=SysImplementUserDataRange.RANGE_ALLOT){
                sysImplementUserTenantService.updateDelFlag(range.getUserId());
            }
            sysImplementUserDataRange.setRoleRange(range.getRoleRange());
            sysImplementUserDataRangeService.saveOrUpdate(sysImplementUserDataRange);
        }

        return Result.success();
    }

}
