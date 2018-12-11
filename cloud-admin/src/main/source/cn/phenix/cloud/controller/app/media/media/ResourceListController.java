package cn.phenix.cloud.controller.app.media.media;

import cn.phenix.cloud.admin.app.media.media.service.ResourceService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.Media;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mgm
 */
@Controller
@Api(tags ="媒资集列表")
@RequestMapping("/platform/media/resource")
public class ResourceListController {
    @Autowired
    private ResourceService resourceService;

    /**
     * resource首页列表
     * @return
     */
    @GetMapping("index")
    @ApiOperation("首页页面跳转")
    public String index(String id, HttpServletRequest req,String levelId,String seriesName,String seriesKeyword,String originalCountry, String categoryId,String publish,String status,
                        String scharge, String echarge, String sreleaseYear, String ereleaseYear, String screateTime,String ecreateTime) {
        req.setAttribute("seriesID",id);

        //以下参数为了带条件返回
        req.setAttribute("levelId",levelId);
        req.setAttribute("scharge",scharge);
        req.setAttribute("echarge",echarge);
        req.setAttribute("sreleaseYear",sreleaseYear);
        req.setAttribute("ereleaseYear",ereleaseYear);
        req.setAttribute("ecreateTime",ecreateTime);
        req.setAttribute("screateTime",screateTime);

        req.setAttribute("seriesName",seriesName);
        req.setAttribute("seriesKeyword",seriesKeyword);
        req.setAttribute("originalCountry",originalCountry);
        req.setAttribute("categoryId",categoryId);
        req.setAttribute("publish",publish);
        req.setAttribute("status",status);
        System.out.println("---------resource首页--------------"+id);
        return "app/media/resource/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @ApiOperation("首页数据加载")
    public PageTable<Media> data(String seriesID,String programCode,String programVolumnCount,
                                 Integer draw, DataTableParameter parameter){
        System.out.println("---------resource获取列表数据--------------");

        PageTable<Media> pageTable = resourceService.findPage(seriesID,programCode,programVolumnCount,parameter);

        pageTable.setDraw(draw);
        return pageTable;
    }


}
