package cn.phenix.cloud.admin.app.media.media.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.Media;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface MediaMapper extends GenericJpaRepository<Media,String> {
    @Modifying
    @Query("DELETE FROM MediaSeries WHERE id = :id")
    void delMedias(@Param("id") String id);

    void deleteBySeriesId(@Param("seriesId") String seriesId);
}

