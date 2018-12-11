package cn.phenix.cloud.admin.app.media.converge.service;

import cn.phenix.cloud.admin.app.media.converge.dao.MediaConvergeMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.media.MediaConverge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mgm
 */
@Service
@Transactional(readOnly = true)
public class MediaConvergeService extends CoreService<MediaConvergeMapper,MediaConverge>{

    @Autowired
    private MediaConvergeMapper mediaConvergeMapper;
    @Transactional
    public void saveList(List<MediaConverge> mediaConvergeList) {
        dao.deleteByMergeIdAndRuleCode(mediaConvergeList.get(0).getMergeId(),mediaConvergeList.get(0).getRuleCode());
        for(MediaConverge mediaConverge:mediaConvergeList){
            dao.deleteByMergeIdAndRuleCodeAndSeriesId(mediaConverge.getMergeId(),mediaConverge.getRuleCode(),mediaConverge.getSeriesId());
            dao.save(mediaConverge);
        }
    }

    public List<MediaConverge> findByRuleCodeAndMergeId(String ruleCode, String mergeId) {
        return dao.findByRuleCodeAndMergeId(ruleCode,mergeId);
    }

    public MediaConverge findByRuleCodeAndSeriesId(String ruleCode, String seriesId) {
        return dao.findByRuleCodeAndSeriesId(ruleCode,seriesId);
    }

    public List<MediaConverge> getListBySeriesId(String seriesId) {

        return mediaConvergeMapper.findByNamedQuery(mediaConvergeMapper.getListBySeriesId,Parameter.create().insert("seriesId",seriesId),MediaConverge.class);
    }
}
