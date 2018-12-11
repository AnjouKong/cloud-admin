package cn.phenix.cloud.controller.tenant.channel;


import cn.phenix.cloud.admin.tenant.channel.service.ChannelService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("platform/tenant/channel")
@Api(tags = "信号源管理")
@RequiresPermissions("sys.manager.unit")
public class ChannelController extends BaseController {

    @Autowired
    private ChannelService channelService;


    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<Channel> data(DataTableParameter parameter, Channel channel) {
        PageTable<Channel> pageTable = channelService.findPage(parameter, channel);
        return pageTable;
    }

    @RequestMapping("add/{tenantId}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add(@PathVariable("tenantId")String tenantId,Model model) {
        model.addAttribute("tenantId",tenantId);
        return "tenant/channel/add";
    }

    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation("添加数据")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(Channel channel) {
        channelService.save(channel);
        return Result.success();
    }


    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", channelService.get(id));
        return "tenant/channel/edit";
    }


    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(Channel channel) {
        channelService.saveOrUpdate(channel);
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        channelService.deleteById(id);
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        channelService.deleteByIds(ids);
        return Result.success();
    }

}
