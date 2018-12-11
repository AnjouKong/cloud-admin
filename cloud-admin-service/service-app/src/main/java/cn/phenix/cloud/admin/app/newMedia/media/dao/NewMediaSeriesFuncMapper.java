package cn.phenix.cloud.admin.app.newMedia.media.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.MediaSeriesFunc;
import cn.phenix.model.app.newMedia.NewMediaSeriesFunc;

import java.util.List;

/**
 * 媒资功能关联
 *
 **/
public interface NewMediaSeriesFuncMapper extends GenericJpaRepository<NewMediaSeriesFunc, String> {

    void deleteBySeriesId(String id);

    List<MediaSeriesFunc> findBySeriesId(String id);
}
