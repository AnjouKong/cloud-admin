package cn.phenix.cloud.admin.app.language.service;

import cn.phenix.cloud.admin.app.language.dao.ApplicationLanguageMapper;
import cn.phenix.cloud.admin.app.language.dao.FieldManagementMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.language.ApplicationLanguage;
import cn.phenix.model.app.language.FieldManagement;
import cn.phenix.model.app.language.ModelManagement;
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
public class ApplicationLanguageService extends CoreService<ApplicationLanguageMapper, ApplicationLanguage> {

    @Transactional
    public void deleteByApplicationCode(String code) {
        dao.deleteByApplicationCode(code);
    }

    public List<ApplicationLanguage> findByDelFlagAndApplicationCode(String s, String code) {
        return dao.findByDelFlagAndApplicationCode(s,code);
    }

    @Transactional
    public void updateDelFlagByApplicationCodeIn(List<String> applicationCodes) {
        dao.updateDelFlagByApplicationCodeIn(applicationCodes);
    }


    public List<ApplicationLanguage> findByApplicationId(String id) {
        Finder finder = Finder.create("select m from ApplicationLanguage m where m.delFlag=:delFlag ")
                .setParam("delFlag", ApplicationLanguage.DEL_FLAG_NORMAL);
        finder.search("m.applicationId", id, Finder.SearchType.EQ);
        return dao.find(finder);
    }
}
