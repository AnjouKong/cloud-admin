package cn.phenix.cloud.controller.app.cms;


import cn.phenix.cloud.admin.app.cms.service.CmsHtmlArticleService;
import cn.phenix.cloud.admin.app.language.service.LanguageDicService;
import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.core.api.Result;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsHtmlArticle;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by wizzer on 2016/6/28.
 */
@Controller
@Api(tags = "新闻管理")
@RequestMapping("/platform/cms/article")
@RequiresPermissions("sys.manager.unit")
public class CmsHtmlArticleController {
    @Autowired
    private CmsHtmlArticleService cmsHtmlArticleService;
    @Autowired
    private AttMainService attMainService;
    @Autowired
    private LanguageDicService languageDicService;


    @RequestMapping("")
    public String index() {
        return "app/cms/article/index";
    }


    @RequestMapping("data")
    @ResponseBody
    public PageTable<CmsHtmlArticle> data(CmsHtmlArticle cmsHtmlArticle, Integer draw, DataTableParameter parameter) {
        PageTable<CmsHtmlArticle> pageTable = cmsHtmlArticleService.findPage(cmsHtmlArticle, parameter);
        pageTable.setDraw(draw);
        return pageTable;
    }


    @PostMapping("addDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result addDo(CmsHtmlArticle article) {
        List<CmsHtmlArticle> list = cmsHtmlArticleService.findByHtmlIdAndLanguageCode(article.getHtmlId(), article.getLanguageCode(), null);
        if (list != null && list.size() > 0) {
            return Result.error("此html资源下已存在相应语言的文章");
        }
        cmsHtmlArticleService.save(article);
        return Result.success();
    }


    @PostMapping("editDo")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result editDo(CmsHtmlArticle article) {
        List<CmsHtmlArticle> list = cmsHtmlArticleService.findByHtmlIdAndLanguageCode(article.getHtmlId(), article.getLanguageCode(), article.getId());
        if (list != null && list.size() > 0) {
            return Result.error("此html资源下已存在语言为<" + article.getLanguageCode() + ">的文章");
        }
        cmsHtmlArticleService.saveOrUpdate(article);
        return Result.success();
    }


//    @PostMapping("/enable/{id}")
//    @ResponseBody
//    @RequiresPermissions("sys.manager.unit")
//    public Result enable(@PathVariable("id") String id) {
//        boolean disable = false;
//        cmsHtmlArticleService.updateById(id, disable);
//        return Result.success();
//    }
//
//    @PostMapping("/disable/{id}")
//    @ResponseBody
//    @RequiresPermissions("sys.manager.unit")
//    public Result disable(@PathVariable("id") String id) {
//        boolean disable = true;
//        cmsHtmlArticleService.updateById(id, disable);
//        return Result.success();
//    }

    @PostMapping("/delete/{oneId}")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result delete(@PathVariable("oneId") String oneId) {
        cmsHtmlArticleService.deleteById(oneId);
        return Result.success();
    }

    @PostMapping("/deletes")
    @ResponseBody
    @RequiresPermissions("sys.manager.unit")
    public Result delete(String[] ids) {
        cmsHtmlArticleService.deleteByIds(ids);
        return Result.success();
    }
}
