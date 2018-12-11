package cn.phenix.cloud.admin.app.newMedia.converge.service;

import cn.phenix.cloud.admin.app.newMedia.converge.dao.NewMediaConvergeMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.newMedia.NewMediaConverge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mgm
 */
@Service
@Transactional(readOnly = true)
public class NewMediaConvergeService extends CoreService<NewMediaConvergeMapper,NewMediaConverge>{

    @Transactional
    public void saveList(List<NewMediaConverge> mediaConvergeList) {
        dao.deleteByMergeIdAndRuleCode(mediaConvergeList.get(0).getMergeId(),mediaConvergeList.get(0).getRuleCode());
        for(NewMediaConverge mediaConverge:mediaConvergeList){
            dao.deleteByMergeIdAndRuleCodeAndSeriesId(mediaConverge.getMergeId(),mediaConverge.getRuleCode(),mediaConverge.getSeriesId());
            dao.save(mediaConverge);
        }
    }

    public List<NewMediaConverge> findByRuleCodeAndMergeId(String ruleCode, String mergeId) {
        return dao.findByRuleCodeAndMergeId(ruleCode,mergeId);
    }

    public NewMediaConverge findByRuleCodeAndSeriesId(String ruleCode, String seriesId) {
        return dao.findByRuleCodeAndSeriesId(ruleCode,seriesId);
    }

    public List<NewMediaConverge> getListBySeriesId(String seriesId) {

        return dao.findByNamedQuery(dao.getListBySeriesId,Parameter.create().insert("seriesId",seriesId),NewMediaConverge.class);
    }
}
