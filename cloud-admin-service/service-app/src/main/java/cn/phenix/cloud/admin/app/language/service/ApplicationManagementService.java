package cn.phenix.cloud.admin.app.language.service;

import cn.phenix.cloud.admin.app.language.dao.ApplicationManagementMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.language.ApplicationLanguage;
import cn.phenix.model.app.language.ApplicationManagement;
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
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationManagementService extends CoreService<ApplicationManagementMapper, ApplicationManagement> {
    @Autowired
    private ApplicationLanguageService applicationLanguageService;
    @Autowired
    private ModelManagementService modelManagementService;

    public PageTable<ApplicationManagement> findPage(ApplicationManagement applicationManagement,DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select m from ApplicationManagement m where m.delFlag=:delFlag ")
                .setParam("delFlag", ApplicationManagement.DEL_FLAG_NORMAL);
        finder.search("name", applicationManagement.getName(), Finder.SearchType.LIKE);
        finder.search("code", applicationManagement.getCode(), Finder.SearchType.EQ);

        Page<ApplicationManagement> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<ApplicationManagement> findByCodeAndDelFlag(String code,String delFlag) {
        return dao.findByCodeAndDelFlag(code,delFlag);
    }

    @Transactional
    public void saveCaseCade(ApplicationManagement applicationManagement,String[] dicCodes) {

        //1.删除关联表
        applicationLanguageService.deleteByApplicationCode(applicationManagement.getCode());

        //3.调用模块删除，删除模块以及模块下字段信息
        List<String> ids = new ArrayList<>();
        ids.add(applicationManagement.getId());
        List<ModelManagement> modelManagementList = modelManagementService.findByApplicationIdIn(ids);
        String[] modelIds = new String[modelManagementList.size()];
        for(int i=0;i<modelManagementList.size();i++){
            modelIds[i] = (modelManagementList.get(i).getId());
        }
        if(modelIds.length>0){
            modelManagementService.updateDelFlagCasecade(modelIds);
        }

        //更新
        for(String dicCode:dicCodes){
            ApplicationLanguage applicationLanguage = new ApplicationLanguage();
            applicationLanguage.setApplicationCode(applicationManagement.getCode());
            applicationLanguage.setLanguageCode(dicCode);
            applicationLanguage.setApplicationId(applicationManagement.getId());
            applicationLanguageService.save(applicationLanguage);
        }
        dao.save(applicationManagement);
    }

    @Transactional
    public void updateDelFlagCasecade(String[] ids) {

        //1.删除应用
        dao.updateDelFlagByIdIn(Arrays.asList(ids));
        //2.删除应用-字典关联表
        List<ApplicationManagement> applicationList = dao.findByIdIn(ids);
        List<String> applicationCodes = new ArrayList<>();
        for(ApplicationManagement applicationManagement:applicationList){
            applicationCodes.add(applicationManagement.getCode());
        }
        applicationLanguageService.updateDelFlagByApplicationCodeIn(applicationCodes);
        //3.调用模块删除，删除模块以及模块下字段信息
        List<ModelManagement> modelManagementList = modelManagementService.findByApplicationIdIn(Arrays.asList(ids));
        String[] modelIds = new String[modelManagementList.size()];
        for(int i=0;i<modelManagementList.size();i++){
            modelIds[i] = (modelManagementList.get(i).getId());
        }
        modelManagementService.updateDelFlagCasecade(modelIds);
    }
}
