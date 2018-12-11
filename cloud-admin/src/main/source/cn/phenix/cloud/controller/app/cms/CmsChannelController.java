package cn.phenix.cloud.controller.app.cms;

import cn.phenix.cloud.admin.app.cms.service.CmsChannelService;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.model.app.cms.CmsChannel;
import io.swagger.annotations.Api;
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

/**
 * Created by wizzer on 2016/6/28.
 */
@Controller
@Api(tags = "cms栏目管理")
@RequestMapping("platform/cms/channel")
public class CmsChannelController {
    @Autowired
    private CmsChannelService cmsChannelService;

    @RequestMapping("")
    public String index(Model model) {
        List<CmsChannel> channelList = cmsChannelService.findByParentIdAndType(null, null, null);
        model.addAttribute("list", channelList);
        return "app/cms/channel/index";
    }

    @GetMapping("add")
    @RequiresPermissions("sys.manager.unit")
    public String add(String pid, Model model) {
        model.addAttribute("obj", Strings.isBlank(pid) ? null : cmsChannelService.get(pid));
        return "app/cms/channel/add";
    }

    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsChannel channel) {
        cmsChannelService.saveAndUpdateHasChildren(channel);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    public String edit(@PathVariable("id") String id, Model model) {
        CmsChannel channel = cmsChannelService.get(id);
        if (channel != null) {
            model.addAttribute("parentMenu", cmsChannelService.get(channel.getParentId()));
        }
        model.addAttribute("obj", channel);
        return "app/cms/channel/edit";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsChannel channel) {
        cmsChannelService.saveOrUpdate(channel);
        return Result.success();
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        cmsChannelService.deleteById(id);
        cmsChannelService.updateHasChildren(cmsChannelService.get(id) == null ? "" : cmsChannelService.get(id).getParentId(), false);
        return Result.success();
    }

    @PostMapping("/enable/{menuId}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result enable(@PathVariable("menuId") String menuId) {
        boolean disable = false;
        cmsChannelService.updateById(menuId, disable);
        return Result.success();
    }

    @PostMapping("/disable/{menuId}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result disable(@PathVariable("menuId") String menuId) {
        boolean disable = true;
        cmsChannelService.updateById(menuId, disable);
        return Result.success();
    }

    @GetMapping("/tree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public List<Map<String, Object>> tree(String pid, String type) {
        List<CmsChannel> list = cmsChannelService.findByParentIdAndType(pid, type, "0");
        List<Map<String, Object>> tree = new ArrayList<>();
        if (Strings.isBlank(pid)) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", "root");
            obj.put("text", "所有栏目");
            obj.put("children", false);
            tree.add(obj);
        }
        for (CmsChannel channel : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", channel.getId());
            obj.put("text", channel.getName());
            obj.put("children", cmsChannelService.isHasChildren(channel.getId()));
            tree.add(obj);
        }
        return tree;
    }

    @GetMapping("/channelTree")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public List<Map<String, Object>> channelTree(String pid, String type) {
        List<CmsChannel> list = cmsChannelService.findByParentIdAndType(pid, type, "0");
        List<Map<String, Object>> tree = new ArrayList<>();
        for (CmsChannel channel : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", channel.getId());
            obj.put("text", channel.getName());
            obj.put("channelType", channel.getType());
           // obj.put("children", channel.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }


    @RequestMapping("/child/{id}")
    public String child(@PathVariable("id") String id, Model model) {
        List<CmsChannel> list = cmsChannelService.findByParentIdAndType(id, null, null);
        model.addAttribute("obj", list);
        return "app/cms/channel/child";
    }

    @RequestMapping("sort")
    @RequiresPermissions("sys.manager.unit")
    public String sort(Model model) {
        List<CmsChannel> firstMenus = cmsChannelService.findByParentIdAndType(null, null, null);
        model.addAttribute("firstMenus", firstMenus);
        return "app/cms/channel/sort";
    }

    @RequestMapping("sortSecond")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result sortSecond(@Param("parentId") String parentId) {
        List<CmsChannel> secondMenus = cmsChannelService.findByParentIdAndType(parentId, null, "0");
        return Result.success().addData(secondMenus);
    }

    @RequestMapping("sortDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result sortDo(@Param("ids") String[] ids) {
        cmsChannelService.updateAllLocation();
        int i = 0;
        for (String id : ids) {
            if (!Strings.isBlank(id)) {
                cmsChannelService.updateLocationById(i, id);
                i++;
            }
        }
        return Result.success();
    }


}
