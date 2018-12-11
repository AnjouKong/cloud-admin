package cn.phenix.cloud.admin.app.newMedia.media.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.newMedia.NewMedia;

import java.util.List;

/**
 * Created by fxq on 2016/12/22.
 */
public interface NewMediaMapper extends GenericJpaRepository<NewMedia,String> {

    List<NewMedia> findBySeriesId(String oldSeriesId);

    void deleteBySeriesId(String id);
}

