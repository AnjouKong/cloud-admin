package cn.phenix.cloud.admin.app.cms.service;

import cn.phenix.cloud.admin.app.cms.dao.CmsHtmlArticleMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.cms.CmsHtmlArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CmsHtmlArticleService extends CoreService<CmsHtmlArticleMapper, CmsHtmlArticle> {


    public PageTable<CmsHtmlArticle> findPage(CmsHtmlArticle cmsHtmlArticle, DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from CmsHtmlArticle");
        finder.whereExcludeDel();
        finder.search("title", cmsHtmlArticle.getTitle(), Finder.SearchType.LIKE);
        finder.search("htmlId", cmsHtmlArticle.getHtmlId());
        finder.append(" order by updateDate desc ");
        Page<CmsHtmlArticle> page = dao.find(finder, pageRequest);

        return new PageTable<>(page);
    }

    public List<CmsHtmlArticle> findByHtmlIdAndLanguageCode(String htmlId, String languageCode, String id) {
        Finder finder = Finder.create("from CmsHtmlArticle");
        finder.whereExcludeDel();

        finder.search("htmlId", htmlId);
        finder.search("languageCode", languageCode);
        finder.search("id", id, Finder.SearchType.NEQ);

        List<CmsHtmlArticle> cmsHtmlArticleList = dao.find(finder);
        return cmsHtmlArticleList;
    }

    @Transactional
    public void updateById(String id, boolean disable) {
        dao.updateById(id, disable);
    }
}
