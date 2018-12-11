package cn.phenix.cloud.controller.app.brand;

import cn.phenix.cloud.admin.app.brand.service.BrandService;
import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationFuncService;
import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceUserExtendService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.brand.BrandSpecification;
import cn.phenix.model.tenant.device.DeviceUserExtends;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


@Controller
@Api(tags = "设备品牌型号管理")
@RequestMapping("platform/device/brandVersion")
public class BrandSpecificationController extends BaseController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandSpecificationService brandSpecificationService;
    @Autowired
    private BrandSpecificationFuncService brandSpecificationFuncService;
    @Autowired
    private DeviceUserExtendService deviceUserExtendService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "品牌型号首页")
    public String index(HttpServletRequest req) {
        req.setAttribute("videoFuncs", brandSpecificationFuncService.findFuncs("1"));
        req.setAttribute("audioFuncs", brandSpecificationFuncService.findFuncs("2"));
        return "app/deviceBrand/version/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "品牌型号首页数据加载")
    public PageTable<BrandSpecification> data(BrandSpecification deviceBrandVersion, @Param("draw") Integer draw, DataTableParameter parameter) {
        PageTable<BrandSpecification> pageTable = brandSpecificationService.findPage(deviceBrandVersion, parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "跳转品牌型号添加页面")
    public String add(Model model,String brandId) {
        //        model.addAttribute("funcs", brandSpecificationFuncService.findFuncs("1"));
        model.addAttribute("videoFuncs", brandSpecificationFuncService.findFuncs("1"));
        model.addAttribute("audioFuncs", brandSpecificationFuncService.findFuncs("2"));
        if(StringUtils.isNotEmpty(brandId) && !brandId.equals("root")){
            model.addAttribute("brand", brandService.get(brandId));
        }
        return "app/deviceBrand/version/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "添加品牌型号")
    public Result addDo(BrandSpecification deviceBrandVersion) {
        List<BrandSpecification> list = brandSpecificationService.findBySpecificationNameAndBrandIdAndDelFlag(deviceBrandVersion.getSpecificationName(),deviceBrandVersion.getBrandId(),"0");
        if(list.size()>0){
            return Result.error("该品牌下此型号已存在");
        }
        brandSpecificationService.addDo(deviceBrandVersion);
        return Result.success();
    }
    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @Transactional
    @ApiOperation(value = "删除品牌型号")
    public Result delete(@PathVariable("id") String id) {

        //如果该型号下有设备，不允许删除
        List<DeviceUserExtends> list =deviceUserExtendService.findBySpecificationId(id);
        if(list.size()>0){
            return Result.error("该设备已经关联设备，请解除关联后再删除！");
        }
        if (!Strings.isBlank(id)){
            brandSpecificationService.delete(id);
        }
        return Result.success();
    }

    @PostMapping("deletes")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @Transactional
    @ApiOperation(value = "批量删除品牌型号")
    public Result deletes(@Param("ids") String[] ids) {
        for(String id :ids){
            List<DeviceUserExtends> list =deviceUserExtendService.findBySpecificationId(id);
            if(list.size()>0){
                return Result.error("该型号已经关联终端，请解除关联后再删除！");
            }
        }
        brandSpecificationService.deletes(ids);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "品牌型号编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
//        model.addAttribute("funcs", brandSpecificationFuncService.findFuncs("1"));
        model.addAttribute("videoFuncs", brandSpecificationFuncService.findFuncs("1"));
        model.addAttribute("audioFuncs", brandSpecificationFuncService.findFuncs("2"));
        BrandSpecification deviceBrandVersion = brandSpecificationService.get(id);
        model.addAttribute("obj",deviceBrandVersion);
        if(StringUtils.isNotEmpty(deviceBrandVersion.getFuncIds())){
            List<String> funcList = Arrays.asList(deviceBrandVersion.getFuncIds().split(","));
            model.addAttribute("funcList",funcList);
        }

        if(StringUtils.isNotEmpty(deviceBrandVersion.getBrandId())){
            model.addAttribute("brand", brandService.get(deviceBrandVersion.getBrandId()));
        }

        return "app/deviceBrand/version/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑品牌型号")
    public Result editDo(BrandSpecification deviceBrandVersion,String oldName) {
        if(!oldName.equals(deviceBrandVersion.getSpecificationName())){
            List<BrandSpecification> list = brandSpecificationService.findBySpecificationNameAndBrandIdAndDelFlag(deviceBrandVersion.getSpecificationName(),deviceBrandVersion.getBrandId(),"0");
            if(list.size()>0){
                return Result.error("该品牌下此型号已存在");
            }

        }

        brandSpecificationService.editDo(deviceBrandVersion);
        return Result.success();
    }

}
