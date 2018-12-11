package cn.phenix.cloud.admin.app.language.service;

import cn.phenix.cloud.admin.app.language.dao.ModelManagementMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.language.FieldManagement;
import cn.phenix.model.app.language.ModelManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mgm
 */
@Service
@Transactional(readOnly = true)
public class ModelManagementService extends CoreService<ModelManagementMapper, ModelManagement> {

    @Autowired
    private FieldLanguageService fieldLanguageService;
    @Autowired
    private FieldManagementService fieldManagementService;

    public PageTable<ModelManagement> findPage(ModelManagement modelManagement, DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select m from ModelManagement m where m.delFlag=:delFlag ")
                .setParam("delFlag", ModelManagement.DEL_FLAG_NORMAL);
        finder.search("name", modelManagement.getName(), Finder.SearchType.LIKE);
        finder.search("code", modelManagement.getCode(), Finder.SearchType.EQ);
        finder.search("applicationManagement.id", modelManagement.getApplicationManagement().getId(), Finder.SearchType.EQ);

        Page<ModelManagement> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<ModelManagement> findByCodeAndDelFlagAndApplicationId(String code, String delFlag, String applicationId) {

        Finder finder = Finder.create("select m from ModelManagement m where m.delFlag=:delFlag ")
                .setParam("delFlag", ModelManagement.DEL_FLAG_NORMAL);
        finder.search("code", code, Finder.SearchType.EQ);
        finder.search("applicationManagement.id", applicationId, Finder.SearchType.EQ);

        return dao.find(finder);
    }

    @Transactional
    public void updateDelFlagCasecade(String[] ids) {
        List<FieldManagement> fieldList = fieldManagementService.findFieldCodeByModelIdIn(Arrays.asList(ids));//字段关联表
        List<String> fieldCodes = new ArrayList<>();
        for (FieldManagement fieldManagement : fieldList) {
            fieldCodes.add(fieldManagement.getCode());
        }

        //id 是模块id即modelId
        //1.字段关联表
        if (fieldCodes.size() > 0) {
            for (String id : ids) {
                fieldLanguageService.updateDelFlagByModelIdAndFieldCodeIn(id, fieldCodes);
            }
        }
        if (ids.length > 0) {
            //2.字段表
            fieldManagementService.updateDelFlagByModelIdIn(Arrays.asList(ids));
            //3..模块表
            dao.updateDelFlagByIdIn(ids);//字段
        }


    }

    public List<ModelManagement> findByApplicationIdIn(List<String> ids) {
        Finder finder = Finder.create("select m from ModelManagement m where m.delFlag=:delFlag ")
                .setParam("delFlag", FieldManagement.DEL_FLAG_NORMAL);
        finder.search("m.applicationManagement.id", ids, Finder.SearchType.IN);
        return dao.find(finder);
    }
    @Transactional
    public void saveCaseCade(ModelManagement modelManagement) {
        fieldManagementService.deleteByModelId(modelManagement.getId());
        dao.save(modelManagement);
    }
}
