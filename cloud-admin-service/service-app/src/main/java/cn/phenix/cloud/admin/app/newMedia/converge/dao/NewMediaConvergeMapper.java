package cn.phenix.cloud.admin.app.newMedia.converge.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.newMedia.NewMediaConverge;

import java.util.List;

/**
 * Created by mgm
 */
public interface NewMediaConvergeMapper extends GenericJpaRepository<NewMediaConverge,String> {
    String getListBySeriesId="getListBySeriesId_new";

    void deleteByMergeIdAndRuleCodeAndSeriesId(String mergeId, String ruleCode, String seriesId);

    List<NewMediaConverge> findByRuleCodeAndMergeId(String ruleCode, String mergeId);

    NewMediaConverge findByRuleCodeAndSeriesId(String ruleCode, String seriesId);

    void deleteByMergeIdAndRuleCode(String mergeId, String ruleCode);
}

