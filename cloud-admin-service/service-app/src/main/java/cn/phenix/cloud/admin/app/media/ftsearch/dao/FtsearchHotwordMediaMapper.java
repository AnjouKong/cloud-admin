package cn.phenix.cloud.admin.app.media.ftsearch.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.ftsearch.FtsearchHotwordMedia;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * @author mgm
 **/
public interface FtsearchHotwordMediaMapper extends GenericJpaRepository<FtsearchHotwordMedia, String> {
    @Modifying
    @Query("DELETE FROM FtsearchHotwordMedia WHERE mediaId = :mediaId and levelId=:levelId")
    void deleteByLevelIdAndMediaId(@Param("levelId")String levelId, @Param("mediaId") String mediaId);
}
