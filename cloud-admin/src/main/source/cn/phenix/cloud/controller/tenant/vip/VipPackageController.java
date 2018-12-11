package cn.phenix.cloud.controller.tenant.vip;

import cn.phenix.cloud.admin.tenant.vip.service.VipContentService;
import cn.phenix.cloud.admin.tenant.vip.service.VipPackageService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.vip.VipContent;
import cn.phenix.model.tenant.vip.VipPackage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author socilents
 *         Create in 2018/5/24 12:01
 **/
@Controller
@RequestMapping("platform/vip/package")
@Api(tags = "套餐包")
@RequiresPermissions("sys.manager.unit")
public class VipPackageController extends BaseController {
    @Autowired
    private VipPackageService vipPackageService;
    @Autowired
    private VipContentService vipContentService;


    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(Model model) {
        return "tenant/vip/package/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation("首页数据加载")
    public PageTable<VipPackage> data(VipPackage vipPackage, Integer draw, DataTableParameter parameter) {
        PageTable<VipPackage> pageTable = vipPackageService.findPage(parameter, vipPackage);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @RequestMapping("/content/{packageId}")
    @ApiOperation("首页跳转")
    public String index(Model model, @PathVariable("packageId") String packageId) {
        model.addAttribute("packageId", packageId);
        return "tenant/vip/content/index";
    }

    @RequestMapping("/content/add")
    @ApiOperation("添加页面跳转")
    public String add(String packageId, Model model) {
        if (StringUtils.isNotEmpty(packageId)) {
            model.addAttribute("packageId", packageId);
            return "tenant/vip/content/add";
        }
        return null;
    }

    @RequestMapping("add")
    @ApiOperation("添加页面跳转")
    public String add(Model model) {
        return "tenant/vip/package/add";
    }

    @PostMapping(value = "/addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    @Transactional
    public Result addDo(VipPackage vipPackage, @Param("upload_Pic_FileName") String upload_Pic_FileName) {
        vipPackage.setBackgroundPic(upload_Pic_FileName);
        vipPackageService.save(vipPackage);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "预览详情")
    public String detail(@PathVariable("id") String id, Model model) {
        VipPackage vipPackage = vipPackageService.get(id);
        List<VipContent> vipContentList = vipContentService.findByPackageId(id);
        model.addAttribute("obj", vipPackage);
        model.addAttribute("content", vipContentList);
        return "tenant/vip/package/detail";
    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        VipPackage vipPackage = vipPackageService.get(id);
        model.addAttribute("obj", vipPackage);
        return "tenant/vip/package/edit";
    }

    @PostMapping(value = "editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑保存")
    public Result editDo(VipPackage vipPackage, @Param("upload_Pic_FileName") String upload_Pic_FileName) {
//        List<VipContent> vipContentList = vipContentService.findByPackageId(vipPackage.getId());
//        vipPackage.setContentList(vipContentList);
        vipPackage.setBackgroundPic(upload_Pic_FileName);
        vipPackageService.saveOrUpdate(vipPackage, "packageName", "note", "backgroundPic");
        return Result.success();
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        vipPackageService.deleteByIds(ids);
        vipContentService.deleteByPackageIds(ids);
        return Result.success();
    }

}
