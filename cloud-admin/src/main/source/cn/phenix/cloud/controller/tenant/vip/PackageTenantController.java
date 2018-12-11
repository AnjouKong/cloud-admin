package cn.phenix.cloud.controller.tenant.vip;

import cn.phenix.cloud.admin.tenant.renter.service.TenancyService;
import cn.phenix.cloud.admin.tenant.vip.service.PackageTenantService;
import cn.phenix.cloud.admin.tenant.vip.service.VipPackageService;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.IdGen;
import cn.phenix.model.sys.SysImplementUserTenant;
import cn.phenix.model.tenant.renter.Tenancy;
import cn.phenix.model.tenant.vip.PackageTenant;
import cn.phenix.model.tenant.vip.VipPackage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * @author socilents
 * Create in 2018/5/25 14:36
 **/
@Controller
@RequestMapping("platform/vip/packageTenant")
@Api(tags = "套餐包-商户信息管理")
@RequiresPermissions("sys.manager.unit")
public class PackageTenantController {

    @Autowired
    private  PackageTenantService packageTenantService;
    @Autowired
    private TenancyService tenancyService;
    @Autowired
    private VipPackageService vipPackageService;

    @GetMapping("selectVipPackage")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择套餐包")
    public String selectVipPackage(Model model, String tenantId) {
        model.addAttribute("tenantId",tenantId);
        return "tenant/tenant/selectVipPackage";
    }

    @RequestMapping(value = "tenantToPackage")
    @ResponseBody
    @ApiOperation(value = "商户添加套餐包")
    @RequiresPermissions("sys.manager.unit")
    public Result tenantToPackage(PackageTenant packageTenant) {
        Tenancy tenancy = tenancyService.get(packageTenant.getTenantId());
        packageTenant.setTenancyName(tenancy.getTenancyName());
        packageTenantService.deleteByTenantId(packageTenant.getTenantId());
        packageTenantService.save(packageTenant);
        return Result.success();
    }

    @GetMapping("addTenant")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("套餐包选择商户")
    public String addTenant(String packageId,Model model) {

        List<Tenancy> tenancyList = tenancyService.getTenantInfoByPackageId(packageId);
        model.addAttribute("packageId",packageId);
        model.addAttribute("obj",tenancyList);
        return "tenant/vip/package/tenant";
    }

    @GetMapping("selectTenant")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择商户页面")
    public String selectTenant(String packageId,Model model) {
        model.addAttribute("packageId",packageId);
        return "tenant/vip/package/selectTenant";
    }

    @PostMapping(value = "packageToTenant")
    @ResponseBody
    @ApiOperation(value = "套餐包选商户Do")
    @RequiresPermissions("sys.manager.unit")
    public Result packageToTenant(PackageTenant packageTenant) {
        String[] tenantIds = packageTenant.getTenantId().split(",");
        String[] tenantNames = packageTenant.getTenancyName().split(",");
        VipPackage vipPackage = vipPackageService.get(packageTenant.getPackageId());
        for(int i=0;i<tenantIds.length;i++){
            packageTenant.setId(IdGen.uuid());
            packageTenant.setUpdateDate(new Date());
            packageTenant.setCreateDate(new Date());
            packageTenant.setDelFlag("0");
            packageTenant.setTenancyName(tenantNames[i]);
            packageTenant.setTenantId(tenantIds[i]);
            packageTenant.setPackageName(vipPackage.getPackageName());
            packageTenantService.deleteByTenantId(tenantIds[i]);
            packageTenantService.save(packageTenant);
        }
        return Result.success();
    }

    @PostMapping("delete")
    @ResponseBody
    @ApiOperation("删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@Param("packageId") String packageId,@Param("tenantId") String tenantId) {
        packageTenantService.deleteByPackageIdAndTenantId(packageId,tenantId);
        return Result.success();
    }

    @PostMapping("deleteVipPackage")
    @ResponseBody
    @ApiOperation("商户端删除vip绑定信息")
    @RequiresPermissions("sys.manager.unit")
    public Result deleteVipPackage(@Param("tenantId") String tenantId) {
        packageTenantService.deleteByTenantId(tenantId);
        return Result.success();
    }


}
