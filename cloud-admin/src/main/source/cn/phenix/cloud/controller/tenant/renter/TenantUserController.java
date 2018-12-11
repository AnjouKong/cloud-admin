package cn.phenix.cloud.controller.tenant.renter;

import cn.phenix.cloud.admin.tenant.renter.service.TenantOfficeService;
import cn.phenix.cloud.admin.tenant.renter.service.TenantUserService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.MD5Util;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.renter.TenantOffice;
import cn.phenix.model.tenant.renter.TenantUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 租户管理
 */
@Controller
@RequestMapping("platform/operate/user")
@Api(tags = "租户管理")
public class TenantUserController extends BaseController {
    @Autowired
    private TenantUserService tenantUserService;
    @Autowired
    private TenantOfficeService tenantOfficeService;

    /**
     * 首页列表
     */

    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(Model model){
        return "tenant/tenantUser/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    @RequiresPermissions("sys.manager.user")
    public PageTable<TenantUser> data(TenantUser tenantUser,Integer draw, DataTableParameter parameter) {

        PageTable<TenantUser> pageTable = tenantUserService.findPage(parameter,tenantUser);
        pageTable.setDraw(draw);

        return pageTable;
    }

    @RequestMapping("add")
    @ApiOperation("添加页面跳转")
    @RequiresPermissions("sys.manager.unit")
    public String add() {
        return "tenant/tenantUser/add";
    }


    @PostMapping(value = "/addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(TenantUser tenantUser) {
       //唯一管理员用户，通过后台管理系统添加
        if (tenantUser!=null) {
            TenantUser u = tenantUserService.findByTenantId(tenantUser);//同一商户下只有一个管理员用户
            if (u != null)
                return Result.error("该商户下管理员用户已存在，请勿重复添加");
        }
        TenantUser u = tenantUserService.findOneByLoginname(tenantUser);
        if (u != null)
            return Result.error("登录名已存在");
        TenantOffice tenantOffice = tenantOfficeService.find4Add(tenantUser);
        if (tenantOffice==null) {
            return Result.error("该商户下没有组织结构，请重新选择");
        }
        tenantUser.setCompany(tenantOffice);//设置组织
        tenantUser.setPassword(MD5Util.getMD5String(tenantUser.getPassword()));//md5加密
        tenantUser.setAdmin(true);
        tenantUserService.save(tenantUser);
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String userId, HttpServletRequest req) {
        TenantUser tenancy = tenantUserService.get(userId);
        tenantUserService.deleteById(userId);
        req.setAttribute("tenancyName", tenancy.getTenantName());
        return Result.success();
    }

    @PostMapping("deletes")
    @ApiOperation(value="批量删除")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Object deletes(@Param("ids") String[] ids, HttpServletRequest req) {
        tenantUserService.deleteByIds(ids);
        req.setAttribute("ids", ids);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @ApiOperation("修改")
    @RequiresPermissions("sys.manager.unit")
    public String edit(@PathVariable String id,Model model) {
        model.addAttribute("obj",tenantUserService.get(id));
        return "tenant/tenantUser/edit";
    }

    @PostMapping("editDo")
    @ApiOperation("修改")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(TenantUser user, @Param("oldLoginname") String oldLoginname) {
        if (!Strings.sBlank(oldLoginname).equals(user.getLoginName())) {
            TenantUser u = tenantUserService.findOneByLoginname(user);
            if (u != null)
                return Result.error("登录名已存在");
        }
        TenantOffice tenantOffice = tenantOfficeService.find4Add(user);
        user.setCompany(tenantOffice);//设置组织
        user.setPassword(MD5Util.getMD5String(user.getPassword()));//md5加密
        user.setAdmin(true);
        tenantUserService.save(user);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("详情")
    @RequiresPermissions("sys.manager.unit")
    public String detail(@PathVariable("id")  String id, Model model) {
        if (!Strings.isBlank(id)) {
            TenantUser user = tenantUserService.get(id);
            model.addAttribute("obj",user);
            return "tenant/tenantUser/detail";
        }
        return null;
    }

}
