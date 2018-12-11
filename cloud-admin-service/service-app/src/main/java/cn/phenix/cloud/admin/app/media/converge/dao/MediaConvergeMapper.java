package cn.phenix.cloud.admin.app.media.converge.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.MediaConverge;

import java.util.List;

/**
 * Created by mgm
 */
public interface MediaConvergeMapper extends GenericJpaRepository<MediaConverge,String> {
    String getListBySeriesId="getListBySeriesId";

    void deleteByMergeIdAndRuleCodeAndSeriesId(String mergeId, String ruleCode, String seriesId);

    List<MediaConverge> findByRuleCodeAndMergeId(String ruleCode, String mergeId);

    MediaConverge findByRuleCodeAndSeriesId(String ruleCode, String seriesId);

    void deleteByMergeIdAndRuleCode(String mergeId, String ruleCode);
}

