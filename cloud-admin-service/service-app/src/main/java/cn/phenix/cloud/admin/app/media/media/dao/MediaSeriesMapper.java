package cn.phenix.cloud.admin.app.media.media.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.MediaSeries;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author xiaobin
 * @create 2017-10-20 下午4:23
 **/
public interface MediaSeriesMapper extends GenericJpaRepository<MediaSeries, String> {
    @Modifying
    @Query("UPDATE MediaSeries SET publish = '0' WHERE id = :id")
    void offLine(@Param("id") String id);
    @Modifying
    @Query("DELETE FROM MediaSeries WHERE id = :id")
    void delMedias(@Param("id") String id);

    List<MediaSeries> findByLevelId(String levelId);

    void deleteByLevelId(String id);

    @Modifying
    @Query("UPDATE MediaSeries SET isRecommend = :isRecommend WHERE id=:mediaId")
    void updateIsRecommend(@Param("mediaId")String mediaId,@Param("isRecommend") String isRecommend);

    @Modifying
    @Query("UPDATE MediaSeries SET isHotWord = :isHotWord WHERE id=:mediaId")
    void updateIsHotWord(@Param("mediaId")String mediaId,@Param("isHotWord") String isHotWord);
}
