package cn.phenix.cloud.admin.app.cms.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.cms.CmsHtmlArticle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CmsHtmlArticleMapper extends GenericJpaRepository<CmsHtmlArticle, String> {


    String deleteCaseCade = "deleteCaseCade";

    @Modifying
    @Query(value="update CmsHtmlArticle set disabled = :disable where id = :id")
    void updateById(@Param("id") String id, @Param("disable") boolean disable);
}
