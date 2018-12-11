package cn.phenix.cloud.admin.app.newMedia.ftsearch.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.newFtsearch.NewSearchHotMedia;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * @author mgm
 **/
public interface NewSearchHotMediaMapper extends GenericJpaRepository<NewSearchHotMedia, String> {
    @Modifying
    @Query("DELETE FROM NewSearchHotMedia WHERE mediaId = :mediaId ")
    void deleteByMediaId(@Param("mediaId") String mediaId);
}
