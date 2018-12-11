package cn.phenix.cloud.controller.app.media.media;

import cn.phenix.cloud.admin.app.media.media.service.MediaLevelService;
import cn.phenix.cloud.admin.app.vo.media.MediaSeriesTagVo;
import cn.phenix.cloud.admin.tenant.renter.service.TenancyService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaLevel;
import cn.phenix.model.tenant.renter.Tenancy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Controller
@Api(tags = "媒资级别")
@RequestMapping("platform/media/level")
public class MediaLevelController extends BaseController {

    @Autowired
    private MediaLevelService mediaLevelService;
    @Autowired
    private TenancyService tenantService;


    @GetMapping("")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页跳转")
    public String index() {
        return "app/media/level/index";
    }

    @PostMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载（不区分状态）")
    public PageTable<MediaLevel> data(@Param("levelName") String levelName, @Param("draw") Integer draw, DataTableParameter parameter) {
        PageTable<MediaLevel> pageTable = mediaLevelService.findPage(levelName,null, parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }
    @PostMapping("selectData")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("启用状态媒资级别数据加载")
    public PageTable<MediaLevel> selectData(@Param("levelName") String levelName, @Param("draw") Integer draw, DataTableParameter parameter) {
        PageTable<MediaLevel> pageTable = mediaLevelService.findPage(levelName,"0", parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "app/media/level/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加数据")
    public Result addDo(MediaLevel mediaLevel,String copyMedia) throws ExecutionException, InterruptedException {
        if (mediaLevelService.get(mediaLevel.getId()) != null)
            return Result.error("系统异常！");
        if (copyMedia.equals("1")) {
            List<MediaSeriesTagVo> ss = mediaLevelService.copy(mediaLevel);
            mediaLevelService.copyLocation(ss);
        }
        mediaLevelService.save(mediaLevel);

        return Result.success();
    }

    @PostMapping("/enable/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("启用级别")
    public Result enable(@PathVariable("id") String id) {
        mediaLevelService.updateStatus(id, "0");
        return Result.success();
    }

    @PostMapping("/disable/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("禁用级别")
    public Result disable(@PathVariable("id") String id) {
        List<Tenancy> tenancyList =tenantService.findByLevelId(id);
        if(tenancyList.size()>0){
            String tenancyName = "";
            for(Tenancy tenancy : tenancyList){
                tenancyName += tenancy.getTenancyName()+"   ";
            }
            return Result.error("禁用失败！该级别下尚有"+tenancyName+"商户存在。 ");
        }
        mediaLevelService.updateStatus(id, "1");
        return Result.success();
    }

    @GetMapping("edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑页面跳转")
    public String edit(@PathVariable("id") String id, Model model) {
        MediaLevel level = mediaLevelService.get(id);
        model.addAttribute("obj", level);
        return "app/media/level/edit";
    }

    @PostMapping("/editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(MediaLevel level) {
        mediaLevelService.saveOrUpdate(level);
        return Result.success();
    }

    @GetMapping("/treeLevel")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("级别树加载")
    public List<Map<String, Object>> treeLevel() {
        List<MediaLevel> list = mediaLevelService.findByDelFlag(MediaLevel.DEL_FLAG_NORMAL);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (MediaLevel level : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", level.getId());
            obj.put("text", level.getLevelName());
            tree.add(obj);
        }
        return tree;
    }

    @GetMapping("/treeLevelForTag")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("标签级别树加载")
    public List<Map<String, Object>> treeLevelForTag() {
        List<MediaLevel> list = mediaLevelService.findByDelFlag(MediaLevel.DEL_FLAG_NORMAL);
        List<Map<String, Object>> tree = new ArrayList<>();
//        Map<String, Object> obj = new HashMap<>();
//        obj.put("id", "root");
//        obj.put("text", "无级别分类");
//        tree.add(obj);
        for (MediaLevel level : list) {
            Map<String, Object> obj2 = new HashMap<>();
            obj2.put("id", level.getId());
            obj2.put("text", level.getLevelName());
            tree.add(obj2);
        }
        return tree;
    }

    @GetMapping("/treeLevelNoBase")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("无根节点级别树加载")
    public List<Map<String, Object>> treeLevelNoBase() {
        List<MediaLevel> list = mediaLevelService.findTreeLevelNoBase();
        List<Map<String, Object>> tree = new ArrayList<>();
        for (MediaLevel level : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", level.getId());
            obj.put("text", level.getLevelName());
            tree.add(obj);
        }
        return tree;
    }

    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        List<Tenancy> tenancyList =tenantService.findByLevelId(id);

        if(tenancyList.size()>0){
            String tenancyName = "";
            for(Tenancy tenancy : tenancyList){
                tenancyName += tenancy.getTenancyName()+"   ";
            }
            return Result.error("删除失败！该级别下尚有"+tenancyName+"商户存在。 ");
        }
        mediaLevelService.delCascade(id);
        return Result.success();
    }


}
