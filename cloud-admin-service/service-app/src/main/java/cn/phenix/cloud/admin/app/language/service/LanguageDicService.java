package cn.phenix.cloud.admin.app.language.service;

import org.springframework.stereotype.Service;
import cn.phenix.cloud.admin.app.language.dao.LanguageDicMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.language.LanguageDic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class LanguageDicService extends CoreService<LanguageDicMapper, LanguageDic> {

    public PageTable<LanguageDic> findPage(LanguageDic languageDic, DataTableParameter parameter) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select m from LanguageDic m where m.delFlag=:delFlag ")
                .setParam("delFlag", LanguageDic.DEL_FLAG_NORMAL);
        finder.search("name", languageDic.getName(), Finder.SearchType.LIKE);
        finder.search("code", languageDic.getCode(), Finder.SearchType.EQ);

        Page<LanguageDic> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<LanguageDic> findByCodeAndDelFlag(String code, String delFlag) {
        return dao.findByCodeAndDelFlag(code, delFlag);
    }

    public List<LanguageDic> findLanguageDic() {
        return dao.findByDelFlag(LanguageDic.DEL_FLAG_NORMAL);
    }

    public List<LanguageDic> findByDelFlag(String delFlag) {
        return dao.findByDelFlag(delFlag);
    }

    public LanguageDic findByCodeAndDelFlag(String code) {
        List<LanguageDic> dic = dao.findByCodeAndDelFlag(code, LanguageDic.DEL_FLAG_NORMAL);
        if (dic == null || dic.size() == 0) return null;
        return dic.get(0);
    }
}
