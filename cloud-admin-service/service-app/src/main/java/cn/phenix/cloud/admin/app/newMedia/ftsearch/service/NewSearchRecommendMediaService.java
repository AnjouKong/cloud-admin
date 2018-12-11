package cn.phenix.cloud.admin.app.newMedia.ftsearch.service;

import cn.phenix.cloud.admin.app.newMedia.ftsearch.dao.NewSearchRecommendMediaMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.newFtsearch.NewSearchRecommendMedia;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mgm
 */
@Service
@Transactional(readOnly = true)
public class NewSearchRecommendMediaService extends CoreService<NewSearchRecommendMediaMapper, NewSearchRecommendMedia> {

    @Transactional
    public void deleteByMediaId(String mediaId) {
        dao.deleteByMediaId(mediaId);
    }
}
