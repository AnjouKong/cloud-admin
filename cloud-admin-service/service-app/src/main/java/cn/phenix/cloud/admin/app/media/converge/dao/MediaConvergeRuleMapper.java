package cn.phenix.cloud.admin.app.media.converge.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.media.MediaConvergeRule;

import java.util.List;

/**
 * Created by mgm
 */
public interface MediaConvergeRuleMapper extends GenericJpaRepository<MediaConvergeRule,String> {

    List<MediaConvergeRule> findByCodeAndDelFlag(String code, String delFlag);

    List<MediaConvergeRule> findByDelFlag(String delFlagNormal);
}

