package cn.phenix.cloud.admin.app.newMedia.converge.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.newMedia.NewMediaConvergeRule;

import java.util.List;

/**
 * Created by mgm
 */
public interface NewMediaConvergeRuleMapper extends GenericJpaRepository<NewMediaConvergeRule,String> {

    List<NewMediaConvergeRule> findByCodeAndDelFlag(String code, String delFlag);

    List<NewMediaConvergeRule> findByDelFlag(String delFlagNormal);
}

