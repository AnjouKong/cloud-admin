package cn.phenix.cloud.controller.sys;

import cn.phenix.cloud.admin.sys.service.SysMenuService;
import cn.phenix.cloud.base.utils.ShiroUtil;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.log.service.PlayEventService;
import cn.phenix.cloud.log.service.SummaryEventService;
import cn.phenix.model.sys.SysMenu;
import cn.phenix.model.sys.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-09-25 下午6:25
 **/
@Controller
@RequestMapping("platform/home")
@Api(tags="首页")
public class PlatformController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private PlayEventService playEventService;
    @Autowired
    private SummaryEventService sumaryEventService;


    @GetMapping("")
    public String home(Model model) throws Exception {
        try{//分权限登陆，必须要获取到当前用户
            SysUser user = ShiroUtil.getUser();
            if(user.isAdmin() || user.getOffice().getCode().equals("yyb")){//管理员和运营部的人可以看统计数据
                return "home_admin";
            }else{
                return "home_other";
            }
        }catch(Exception e){
            throw new Exception("系统错误，请联系管理员.");
        }

    }

    /**
     * 左侧导航
     *
     * @return
     */
    @GetMapping("left")
    @ApiOperation("左侧导航")
    public String left(String parentId, String path, Model model) {
        model.addAttribute("parentId", parentId);
        if (StringUtils.isNotBlank(path)) {
            path = StringUtils.remove(path, "javascript:;");
        }
        model.addAttribute("path", path);
        return "left";
    }

    @GetMapping("path")
    @ApiOperation("路径加载")
    public String path(String url, Model model, HttpServletRequest request) {
        if (Strings.sBlank(url).indexOf("//") > 0) {
            String[] u = url.split("//");
            String s = u[1].substring(u[1].indexOf("/"));
            if (Strings.sBlank(s).indexOf("?") > 0)
                s = s.substring(0, s.indexOf("?"));
            String[] urls = s.split("/");
            List<String> list = new ArrayList<>();
            if (urls.length > 5) {
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3] + "/" + urls[4] + "/" + urls[5]);
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3] + "/" + urls[4]);
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3]);
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 5) {
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3] + "/" + urls[4]);
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3]);
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 4) {
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3]);
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 3) {
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 2) {
                list.add("/" + urls[1]);
            } else list.add(url);
            model.addAttribute("pid", sysMenuService.findByHrefLoadParentId(list));
            SysMenu menu = sysMenuService.findByHrefLoadRootParent(list);
            if (menu != null) {
                model.addAttribute("parentId", menu.getId());
            }else{
                model.addAttribute("parentId", "");
            }
        }else{
            model.addAttribute("parentId", "");
            model.addAttribute("pid", "");
        }
        return "left";
    }

    @GetMapping("/dataAnalysis")
    @ResponseBody
    @ApiOperation("首页数据填充")
    public Map<String,Object> dataAnalysis() {
        Map map =new HashMap();
        map.put("top10",playEventService.findTop10());
        map.put("sum",sumaryEventService.findSum());
        return map;
    }
}
