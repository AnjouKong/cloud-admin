package cn.phenix.cloud.admin.app.language.service;

import cn.phenix.cloud.admin.app.language.dao.FieldManagementMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.language.FieldLanguage;
import cn.phenix.model.app.language.FieldManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class FieldManagementService extends CoreService<FieldManagementMapper, FieldManagement> {

    @Autowired
    private FieldLanguageService fieldLanguageService;
    @Autowired
    private FieldManagementMapper fieldManagementMapper;

    public PageTable<FieldManagement> findPage(FieldManagement fieldManagement,DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select m from FieldManagement m where m.delFlag=:delFlag ")
                .setParam("delFlag", FieldManagement.DEL_FLAG_NORMAL);
        finder.search("modelManagement.id", fieldManagement.getModelManagement().getId(), Finder.SearchType.EQ);
        finder.search("name", fieldManagement.getName(), Finder.SearchType.LIKE);
        finder.search("code", fieldManagement.getCode(), Finder.SearchType.EQ);

        Page<FieldManagement> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<FieldManagement> findByCodeAndDelFlagAndModelId(String code,String delFlag,String modelId) {
        Finder finder = Finder.create("select m from FieldManagement m where m.delFlag=:delFlag ")
                .setParam("delFlag", FieldManagement.DEL_FLAG_NORMAL);
        finder.search("code", code, Finder.SearchType.EQ);
        finder.search("modelManagement.id", modelId, Finder.SearchType.EQ);

        return dao.find(finder);
    }

    @Transactional
    public void saveCaseCade(FieldManagement fieldManagement, List<FieldLanguage> fieldLanguageList) {
        fieldLanguageService.deleteByFieldCodeAndModelId(fieldManagement.getCode(),fieldManagement.getModelManagement().getId());
        for(FieldLanguage fieldLanguage:fieldLanguageList){
            fieldLanguageService.save(fieldLanguage);
        }
        dao.save(fieldManagement);
    }
    @Transactional
    public void updateDelFlagCasecade(String[] ids,String modelId) {
        for(String id :ids){
            FieldManagement fieldManagement = dao.getOne(id);
            List<FieldLanguage> fieldLanguageList = fieldLanguageService.findByDelFlagAndFieldCodeAndModelId("0",fieldManagement.getCode(),fieldManagement.getModelManagement().getId());

            if(fieldLanguageList.size()>0){
                for(FieldLanguage fieldLanguage:fieldLanguageList){
                    fieldLanguageService.updateDelFlagByFieldCodeAndModelId(fieldLanguage.getFieldCode(),modelId);//字段关联表
                }
            }
            dao.deleteById(id);//字段
        }
    }

    public List<FieldManagement> findFieldCodeByModelIdIn(List<String> ids) {
        Finder finder = Finder.create("select m from FieldManagement m where m.delFlag=:delFlag ")
                .setParam("delFlag", FieldManagement.DEL_FLAG_NORMAL);
        finder.search("m.modelManagement.id",ids, Finder.SearchType.IN);
        return dao.find(finder);
    }

    @Transactional
    public void updateDelFlagByModelIdIn(List<String> ids) {
        dao.updateDelFlagByModelIdIn(ids);
    }
    @Transactional
    public void deleteByModelId(String modelId) {
        dao.deleteByModelId(modelId);
    }
}
