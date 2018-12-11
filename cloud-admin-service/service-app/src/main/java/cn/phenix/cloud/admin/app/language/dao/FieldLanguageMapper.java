package cn.phenix.cloud.admin.app.language.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.app.language.FieldLanguage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**

* Created by mgm
 */
public interface FieldLanguageMapper extends GenericJpaRepository<FieldLanguage,String> {
    void deleteByFieldCodeAndModelId(String code, String modelId);

    List<FieldLanguage> findByDelFlagAndFieldCodeAndModelId(String s, String code,String modelId);

    @Modifying
    @Query("update FieldLanguage set delFlag='1' where fieldCode=:fieldCode and modelId=:modelId")
    void updateDelFlagByFieldCodeAndModelId(@Param("fieldCode") String fieldCode,@Param("modelId") String modelId);

    @Modifying
    @Query("update FieldLanguage set delFlag='1' where fieldCode in :fieldCode and modelId=:modelId")
    void updateDelFlagByModelIdAndFieldCodeIn(@Param("modelId") String modelId,@Param("fieldCode") List<String> fieldCodes);

}

