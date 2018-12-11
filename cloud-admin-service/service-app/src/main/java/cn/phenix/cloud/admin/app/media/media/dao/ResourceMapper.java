package cn.phenix.cloud.admin.app.media.media.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.Media;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface ResourceMapper extends GenericJpaRepository<Media,String> {

    List<Media> findBySeriesId(String oldSeriesId);

    void deleteBySeriesId(String id);
}

