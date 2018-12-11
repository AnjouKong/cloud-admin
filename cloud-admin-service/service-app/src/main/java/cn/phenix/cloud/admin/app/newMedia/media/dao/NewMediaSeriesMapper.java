package cn.phenix.cloud.admin.app.newMedia.media.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.newMedia.NewMediaSeries;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author fxq
 **/
public interface NewMediaSeriesMapper extends GenericJpaRepository<NewMediaSeries, String> {
    @Modifying
    @Query("UPDATE NewMediaSeries SET publish = '0' WHERE id = :id")
    void offLine(@Param("id") String id);

    @Modifying
    @Query("UPDATE NewMediaSeries SET publish = '1' WHERE id = :id")
    void release(@Param("id") String id);

    @Modifying
    @Query("DELETE FROM NewMediaSeries WHERE id = :id")
    void delMedias(@Param("id") String id);

    @Modifying
    @Query("UPDATE NewMediaSeries SET isRecommend = :isRecommend WHERE id=:mediaId")
    void updateIsRecommend(@Param("mediaId") String mediaId, @Param("isRecommend") String isRecommend);

    @Modifying
    @Query("UPDATE NewMediaSeries SET isHotWord = :isHotWord WHERE id=:mediaId")
    void updateIsHotWord(@Param("mediaId") String mediaId, @Param("isHotWord") String isHotWord);
}
