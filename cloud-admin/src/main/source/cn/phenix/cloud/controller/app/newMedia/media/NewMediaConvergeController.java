package cn.phenix.cloud.controller.app.newMedia.media;


import cn.phenix.cloud.admin.app.brand.service.BrandSpecificationFuncService;
import cn.phenix.cloud.admin.app.newMedia.converge.service.NewMediaConvergeService;
import cn.phenix.cloud.admin.app.newMedia.media.service.NewMediaSeriesService;
import cn.phenix.cloud.admin.sys.service.SysDictService;
import cn.phenix.cloud.base.web.BaseController;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.common.utils.StringUtils;
import cn.phenix.model.app.newMedia.NewMediaConverge;
import cn.phenix.model.sys.SysDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * Created by mgm
 */
@Controller
@Api(tags = "媒资聚合")
@RequestMapping("/platform/newMedia/converge")
public class NewMediaConvergeController extends BaseController {
    @Autowired
    private BrandSpecificationFuncService deviceBrandVersionFuncService;
    @Autowired
    private NewMediaSeriesService mediaSeriesService;
    @Autowired
    private NewMediaConvergeService mediaConvergeService;
    @Autowired
    private SysDictService sysDictService;


    @RequestMapping("")
    @ApiOperation("首页跳转")
    public String index(HttpServletRequest req, String id,
                        String seriesName, String seriesKeyword, String originalCountry, String categoryId, String publish,
                        String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime, String ecreateTime) {

        //影片、型号功能 如4k 蓝光、杜比等
//        req.setAttribute("funcs",deviceBrandVersionFuncService.findFuncs("1"));
        req.setAttribute("videoFuncs", deviceBrandVersionFuncService.findFuncs("1"));
        req.setAttribute("audioFuncs", deviceBrandVersionFuncService.findFuncs("2"));
        req.setAttribute("obj", mediaSeriesService.get(id));
        //以下参数为了带条件返回
        req.setAttribute("scharge", scharge);
        req.setAttribute("echarge", echarge);
        req.setAttribute("sreleaseYear", sreleaseYear);
        req.setAttribute("ereleaseYear", ereleaseYear);
        req.setAttribute("ecreateTime", ecreateTime);
        req.setAttribute("screateTime", screateTime);

        req.setAttribute("seriesName", seriesName);
        req.setAttribute("seriesKeyword", seriesKeyword);
        req.setAttribute("originalCountry", originalCountry);
        req.setAttribute("categoryId", categoryId);
        req.setAttribute("publish", publish);


        return "app/newMedia/converge/index";
    }

    @GetMapping("selectScreenSaverResource")
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("选择聚合媒资页面")
    public String selectScreenSaverResource(HttpServletRequest req, String levelId) {
//        req.setAttribute("funcs",deviceBrandVersionFuncService.findFuncs("1"));
        req.setAttribute("levelId", levelId);
        return "app/newMedia/converge/selectMediaSeries";
    }

    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    @ApiOperation("编辑数据")
    public Result editDo(HttpServletRequest req, NewMediaConverge mediaConverge) {
        if (mediaConverge != null) {
            List<NewMediaConverge> mediaConvergeList = new ArrayList<>();
            String[] seriesId = mediaConverge.getSeriesId().split(",");
            String[] seriesName = mediaConverge.getSeriesNames().split(",");
            List<NewMediaConverge> ss = mediaConvergeService.findByRuleCodeAndMergeId(mediaConverge.getRuleCode(), seriesId[0]);

            List<String> funcList = new LinkedList<String>();

            for (int i = 0; i < seriesId.length; i++) {
                NewMediaConverge temp = new NewMediaConverge();
                String funcId = req.getParameter(seriesId[i]);
                if (funcList.contains(funcId)) {
                    return Result.error("【" + seriesName[i] + "】属性重复！");
                } else {
                    funcList.add(funcId);
                }
                if (StringUtils.isEmpty(funcId)) {
                    return Result.error("【" + seriesName[i] + "】尚未选择属性！");
                }
                if (ss.size() > 0) {//存在聚合过的媒资
                    temp.setMergeId(ss.get(0).getMergeId());
                } else {
                    temp.setMergeId(seriesId[0]);
                }
                temp.setRuleCode(mediaConverge.getRuleCode());
                temp.setFuncId(funcId);
                temp.setSeriesId(seriesId[i]);
                temp.setSeriesNames(seriesName[i]);
                temp.setFuncName(sysDictService.get(funcId).getName());
                mediaConvergeList.add(temp);
            }
            mediaConvergeService.saveList(mediaConvergeList);
        }
        return Result.success();
    }
//    @RequestMapping("getConvergeList")
//    @ResponseBody
//    public List<MediaConverge> getConvergeList(@Param("ruleCode") String ruleCode, @Param("seriesId") String seriesId) {
//        MediaConverge mediaConverge = mediaConvergeService.findByRuleCodeAndSeriesId(ruleCode, seriesId);
//        if(mediaConverge != null){
//            return mediaConvergeService.findByRuleCodeAndMergeId(ruleCode, mediaConverge.getMergeId());
//        }else{
//            return null;
//        }
//    }

    @RequestMapping("getConvergeList")
    @ResponseBody
    @ApiOperation("获取媒资聚合列表")
    public Map<String, List<Object>> getConvergeList(@Param("ruleCode") String ruleCode, @Param("seriesId") String seriesId) {
        Map map = new HashMap<>();
        NewMediaConverge mediaConverge = mediaConvergeService.findByRuleCodeAndSeriesId(ruleCode, seriesId);
        List<SysDict> sysDictList = sysDictService.findByParentId(ruleCode);
        if (mediaConverge != null) {
            map.put("convergeList", mediaConvergeService.findByRuleCodeAndMergeId(ruleCode, mediaConverge.getMergeId()));
            map.put("sysDictList", sysDictList);
            return map;
        }
        return null;

    }

    @RequestMapping("getSysDic")
    @ResponseBody
    @ApiOperation("获取地区列表")
    public List<SysDict> getSysDic(@Param("ruleCode") String ruleCode) {
        List<SysDict> sysDictList = sysDictService.findByParentId(ruleCode);
        if (sysDictList.size() > 0) {
            return sysDictList;
        } else {
            return null;
        }
    }

}
