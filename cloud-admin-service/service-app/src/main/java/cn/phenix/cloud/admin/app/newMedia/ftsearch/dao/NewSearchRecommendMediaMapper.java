package cn.phenix.cloud.admin.app.newMedia.ftsearch.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.newFtsearch.NewSearchRecommendMedia;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * @author mgm
 **/
public interface NewSearchRecommendMediaMapper extends GenericJpaRepository<NewSearchRecommendMedia, String> {

    @Modifying
    @Query("DELETE FROM NewSearchRecommendMedia WHERE mediaId = :mediaId ")
    void deleteByMediaId(@Param("mediaId") String mediaId);
}
