package cn.phenix.cloud.admin.app.language.service;


import cn.phenix.cloud.admin.app.language.dao.FieldLanguageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.app.language.FieldLanguage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class FieldLanguageService extends CoreService<FieldLanguageMapper, FieldLanguage> {

    @Transactional
    public void deleteByFieldCodeAndModelId(String code,String modelId) {
        dao.deleteByFieldCodeAndModelId(code,modelId);
    }

    public List<FieldLanguage> findByDelFlagAndFieldCodeAndModelId(String s, String code,String modelId) {
        return dao.findByDelFlagAndFieldCodeAndModelId(s,code,modelId);
    }

    @Transactional
    public void updateDelFlagByFieldCodeAndModelId(String fieldCode,String modelId) {
        dao.updateDelFlagByFieldCodeAndModelId(fieldCode,modelId);
    }

    @Transactional
    public void updateDelFlagByModelIdAndFieldCodeIn(String modelId,List<String> fieldCodes) {
         dao.updateDelFlagByModelIdAndFieldCodeIn(modelId,fieldCodes);
    }

}
