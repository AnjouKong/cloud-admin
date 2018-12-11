package cn.phenix.cloud.controller.app.brand;

import cn.phenix.cloud.admin.app.brand.service.BrandService;
import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationFuncService;
import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.brand.Brand;
import cn.phenix.model.app.brand.BrandSpecification;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Api(tags = "设备品牌管理")
@RequestMapping("platform/device/brand")
public class BrandController extends BaseController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandSpecificationService brandSpecificationService;
    @Autowired
    private BrandSpecificationFuncService deviceBrandVersionFuncService;

    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "首页跳转")
    public String index() {
        return "app/deviceBrand/brand/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "首页数据")
    public PageTable<Brand> data(Brand deviceBrand, @Param("draw") Integer draw, DataTableParameter parameter) {
        PageTable<Brand> pageTable = brandService.findPage(deviceBrand, parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "新加品牌跳转")
    public String add(Model model) {
        model.addAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
        model.addAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
        return "app/deviceBrand/brand/add";
    }


    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @Transactional
    @ApiOperation(value = "新加品牌")
    public Result addDo(Brand deviceBrand, BrandSpecification deviceBrandVersion) {
        if (brandService.get(deviceBrand.getId()) != null)
            return Result.error("系統異常");
        Integer size = brandService.findByBrandName(deviceBrand.getBrandName());
        if (size > 0) {
            return Result.error("品牌名称已存在");
        }
        deviceBrandVersion.setBrandId(deviceBrand.getId());
        brandService.save(deviceBrand);
        if(StringUtils.isNoneBlank(deviceBrandVersion.getSpecificationName())){
            brandSpecificationService.addDo(deviceBrandVersion);
        }
        return Result.success();
    }
    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "删除品牌")
    public Result delete(@PathVariable("id") String id) {
        if (!Strings.isBlank(id)){
            List<BrandSpecification> list = brandSpecificationService.findByBrandIdAndDelFlag(id,"0");
            if(list.size()>0){
                String str = "该品牌下有"+list.size()+"个型号存在，请删除后再操作！";
                return Result.error(str);
            }
            brandService.deleteCascade(id);
        }
        return Result.success();
    }

    @PostMapping("delete")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "批量删除")
    public Result deletes(@Param("ids") String ids) {
        for(String id: ids.split(",")){
            List<BrandSpecification> list = brandSpecificationService.findByBrandIdAndDelFlag(id,"0");
            if(list.size()>0){
                String str = "选中品牌下有"+list.size()+"个型号存在，请删除后再操作！";
                return Result.error(str);
            }
            brandService.deleteCascade(id);
        }
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        Brand deviceBrand = brandService.get(id);
        model.addAttribute("brand",deviceBrand);

        return "app/deviceBrand/brand/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑操作")
    public Result editDo(Brand deviceBrand,String oldbrandName) {
        if(!oldbrandName.equals(deviceBrand.getBrandName())){
            Integer size = brandService.findByBrandName(deviceBrand.getBrandName());
            if (size > 0) {
                return Result.error("品牌名称已存在");
            }

        }
        brandService.saveOrUpdate(deviceBrand);
        return Result.success();
    }
    @GetMapping("/brandTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "品牌树")
    public List<Map<String, Object>> brandTree() {
        List<Brand> list = brandService.findByDelFlag(Brand.DEL_FLAG_NORMAL);
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        obj.put("id", "root");
        obj.put("text", "全部型号");
        tree.add(obj);
        for (Brand deviceBrand : list) {
            Map<String, Object> obj2 = new HashMap<>();
            obj2.put("id", deviceBrand.getId());
            obj2.put("text", deviceBrand.getBrandName());
            tree.add(obj2);
        }
        return tree;
    }

    @GetMapping("/secondTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "品牌-型号树")
    public List<Map<String, Object>> secondTree(String brandId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        if(StringUtils.isBlank(brandId)){
            List<Brand> list = brandService.findByDelFlag(Brand.DEL_FLAG_NORMAL);
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", "root");
            obj.put("text", "全部型号");
            tree.add(obj);
            for (Brand deviceBrand : list) {
                Map<String, Object> obj2 = new HashMap<>();
                obj2.put("id", deviceBrand.getId());
                obj2.put("text", deviceBrand.getBrandName());
                obj2.put("children", true);
                tree.add(obj2);
            }
        }else{
            List<BrandSpecification> list = brandSpecificationService.findByBrandIdAndDelFlag(brandId,"0");
            for (BrandSpecification specification : list) {
                Map<String, Object> obj2 = new HashMap<>();
                obj2.put("id", specification.getId());
                obj2.put("text", specification.getSpecificationName());
                obj2.put("children", false);
                tree.add(obj2);
            }
        }
        return tree;
    }
    @GetMapping("/strategyTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "策略树")
    public List<Map<String, Object>> strategyTree(String brandId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        if(StringUtils.isBlank(brandId)){
            List<Brand> list = brandService.findByDelFlag(Brand.DEL_FLAG_NORMAL);
             for (Brand deviceBrand : list) {
                Map<String, Object> obj2 = new HashMap<>();
                obj2.put("id", deviceBrand.getId());
                obj2.put("text", deviceBrand.getBrandName());
                obj2.put("children", true);
                tree.add(obj2);
            }
        }else{
            List<BrandSpecification> list = brandSpecificationService.findByBrandIdAndDelFlag(brandId,"0");
            for (BrandSpecification specification : list) {
                Map<String, Object> obj2 = new HashMap<>();
                obj2.put("id", specification.getId());
                obj2.put("text", specification.getSpecificationName());
                obj2.put("children", false);
                tree.add(obj2);
            }
        }
        return tree;
    }
}
