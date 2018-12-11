package cn.phenix.cloud.admin.app.media.ftsearch.service;

import cn.phenix.cloud.admin.app.media.ftsearch.dao.FtsearchRecommendMediaMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.ftsearch.FtsearchRecommendMedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mgm
 */
@Service
@Transactional(readOnly = true)
public class FtsearchRecommendMediaService extends CoreService<FtsearchRecommendMediaMapper,FtsearchRecommendMedia>{

    @Autowired
    private  FtsearchRecommendMediaMapper ftsearchRecommendMediaMapper;
    @Transactional
    public void deleteByLevelIdAndMediaId(String levelId, String mediaId) {
        ftsearchRecommendMediaMapper.deleteByLevelIdAndMediaId(levelId,mediaId);
    }
}
