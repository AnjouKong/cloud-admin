package cn.phenix.cloud.admin.app.language.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.language.ApplicationLanguage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**

* Created by mgm
 */
public interface ApplicationLanguageMapper extends GenericJpaRepository<ApplicationLanguage,String> {

    void deleteByApplicationCode(String code);

    List<ApplicationLanguage> findByDelFlagAndApplicationCode(String s, String code);

    @Modifying
    @Query("update ApplicationLanguage set delFlag='1' where applicationCode in :applicationCodes")
    void updateDelFlagByApplicationCodeIn(@Param("applicationCodes") List<String> applicationCodes);
}

