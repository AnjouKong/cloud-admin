package cn.phenix.cloud.admin.app.language.dao;//package cn.phenix.cloud.admin.tenant.verify.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.language.LanguageDic;
import java.util.List;

/**

* Created by mgm
 */
public interface LanguageDicMapper extends GenericJpaRepository<LanguageDic,String> {

    List<LanguageDic> findByCodeAndDelFlag(String code, String delFlag);

    List<LanguageDic> findByDelFlag(String delFlag);
}

