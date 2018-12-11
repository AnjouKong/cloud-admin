package cn.phenix.cloud.controller.tenant.vip;

import cn.phenix.cloud.admin.tenant.vip.service.VipContentService;
import cn.phenix.cloud.admin.tenant.vip.service.VipPackageService;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


/**
 * @author socilents
 * Create in 2018/5/24 15:28
 **/
@Controller
@RequestMapping("platform/vip/content")
@Api(tags = "套餐包内容")
@RequiresPermissions("sys.manager.unit")
public class VipContentController {
    @Autowired
    private VipContentService vipContentService;
    @Autowired
    private VipPackageService vipPackageService;

    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(Model model,String packageId) {
        model.addAttribute("packageId",packageId);
        return "tenant/vip/content/index";
    }


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.user")
    @ApiOperation("首页数据加载")
    public PageTable<VipContent> data(VipContent vipContent ,String packageId, Integer draw, DataTableParameter parameter) {

        VipPackage vipPackage=vipPackageService.get(packageId);
        PageTable<VipContent> pageTable = vipContentService.findPage(parameter, vipContent,packageId);
        pageTable.getData().forEach(vipContent1->vipContent1.setPackageName(vipPackage.getPackageName()));
        pageTable.setDraw(draw);

        return pageTable;
    }

    @RequestMapping("add")
    @ApiOperation("添加页面跳转")
    public String add(String packageId,Model model) {
        if(StringUtils.isNotEmpty(packageId)){
            model.addAttribute("packageId",packageId);
            return "tenant/vip/content/add";
        }
        return null;
    }

    @PostMapping(value = "/addDo")
    @ResponseBody
    @ApiOperation(value = "增加保存")
    @RequiresPermissions("sys.manager.unit")
    @Transactional
    public Result addDo(VipContent vipContent) {
        Long timeStrap=TimeUnit.valueOf(vipContent.getTimeUnit()).toMillis(Long.valueOf(vipContent.getTime()));
        if(vipContent.getIsRecommend().equals("1")){//设为推荐
            vipContentService.updateByPackageId(vipContent.getVipPackage().getId());
        }
        vipContent.setTimeStrap(timeStrap.toString());
        vipContentService.save(vipContent);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "预览详情")
    public String detail(@PathVariable("id") String id, Model model) {
        VipContent vipContent = vipContentService.get(id);
        model.addAttribute("obj", vipContent);
        return "tenant/vip/content/detail";
    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        VipContent vipContent = vipContentService.get(id);
        model.addAttribute("obj", vipContent);
        return "tenant/vip/content/edit";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(VipContent vipContent) {
        Long timeStrap=TimeUnit.valueOf(vipContent.getTimeUnit()).toMillis(Long.valueOf(vipContent.getTime()));
        vipContent.setTimeStrap(timeStrap.toString());
        if(vipContent.getIsRecommend().equals("1")){//设为推荐
            vipContentService.updateByPackageId(vipContent.getVipPackage().getId());
        }
        vipContentService.saveOrUpdate(vipContent);
        return Result.success();
    }

    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        vipContentService.deleteByIds(ids);
        return Result.success();
    }
}
