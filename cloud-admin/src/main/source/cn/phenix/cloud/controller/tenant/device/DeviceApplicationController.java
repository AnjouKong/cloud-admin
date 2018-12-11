package cn.phenix.cloud.controller.tenant.device;


import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.admin.mechanism.vo.AttMainForm;
import cn.phenix.cloud.admin.tenant.device.service.DeviceApplicationService;
import cn.phenix.cloud.admin.tenant.device.service.DeviceVersionService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.mechanism.AttMain;
import cn.phenix.model.tenant.device.DeviceApplication;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("platform/operate/application")
@Api(tags = "终端版本管理")
@RequiresPermissions("sys.manager.unit")
public class DeviceApplicationController extends BaseController {

    @Autowired
    private DeviceApplicationService deviceApplicationService;
    @Autowired
    private DeviceVersionService deviceVersionService;
    @Autowired
    private AttMainService attMainService;


    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index() {
        return "tenant/application/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("首页数据加载")
    public PageTable<DeviceApplication> data(DataTableParameter parameter, DeviceApplication deviceApplication) {
        PageTable<DeviceApplication> pageTable = deviceApplicationService.findPage(parameter, deviceApplication);
        return pageTable;
    }

    @RequestMapping("add")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("添加页面跳转")
    public String add() {
        return "tenant/application/add";
    }

    @PostMapping(value = "addDo")
    @ResponseBody
    @ApiOperation("添加数据")
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(DeviceApplication deviceApplication, AttMainForm attMainForm) {
        deviceApplicationService.save(deviceApplication);
        attMainService.updateAttMainForm(attMainForm, deviceApplication.getId(), DeviceApplication.modelName);
        return Result.success();
    }

    @PostMapping(value = "download")
    @ResponseBody
    @ApiOperation(value = "下载")
    @RequiresPermissions("sys.manager.unit")
    public void download(DeviceApplication deviceApplication, HttpServletResponse response) throws IOException {
        List<AttMain> list = attMainService.getAttMain(deviceApplication.getId(), DeviceApplication.modelName);
        if(list.size()==0) throw new IOException("下载的文件不存在");
        URL url = new URL(list.get(0).getFdFilePath());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + list.get(0).getFdFileName() + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        while ((len = inputStream.read(b)) > 0)
            response.getOutputStream().write(b, 0, len);
        inputStream.close();

    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "编辑")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("obj", deviceApplicationService.get(id));
        return "tenant/application/edit";
    }

    @GetMapping("/version/{id}")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation(value = "版本信息")
    public String version(@PathVariable("id") String id, Model model) {
        model.addAttribute("applicationId", id);
        return "tenant/application/version/index";
    }

    @RequestMapping(value = "editDo")
    @ResponseBody
    @ApiOperation(value = "编辑保存")
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(DeviceApplication deviceApplication) {
        deviceApplicationService.saveOrUpdate(deviceApplication);
        return Result.success();
    }


    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("id") String id) {
        deviceApplicationService.deleteById(id);
        deviceVersionService.updateDelFlagByApplication(id);
        attMainService.deleteAttMain(id, DeviceApplication.modelName);
        return Result.success();
    }


    @PostMapping(value = "deletes")
    @ResponseBody
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("sys.manager.unit")
    public Result deletes(String[] ids) {
        deviceApplicationService.deleteByIds(ids);
        for (String str : ids) {
            deviceVersionService.updateDelFlagByApplication(str);
            attMainService.deleteAttMain(str, DeviceApplication.modelName);
        }
        return Result.success();
    }

}
