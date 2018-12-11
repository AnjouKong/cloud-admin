package cn.phenix.cloud.admin.app.media.media.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.MediaSeriesFunc;

import java.util.List;

/**
 * 媒资功能关联
 *
 **/
public interface MediaSeriesFuncMapper extends GenericJpaRepository<MediaSeriesFunc, String> {

    void deleteBySeriesId(String id);

    List<MediaSeriesFunc> findBySeriesId(String id);
}
