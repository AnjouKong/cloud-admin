package cn.phenix.cloud.admin.app.newMedia.converge.service;

import cn.phenix.cloud.admin.app.newMedia.converge.dao.NewMediaConvergeRuleMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaConvergeRule;
import cn.phenix.model.app.newMedia.NewMediaConvergeRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mgm
 */
@Service
@Transactional(readOnly = true)
public class NewMediaConvergeRuleService extends CoreService<NewMediaConvergeRuleMapper, NewMediaConvergeRule> {


    public PageTable<NewMediaConvergeRule> findPage(DataTableParameter parameter, NewMediaConvergeRule mediaConvergeRule) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from NewMediaConvergeRule where delFlag=:delFlag ")
                .setParam("delFlag", MediaConvergeRule.DEL_FLAG_NORMAL);
        finder.search("name", mediaConvergeRule.getName(), Finder.SearchType.LIKE);
        finder.search("code", mediaConvergeRule.getCode(), Finder.SearchType.EQ);

        Page<NewMediaConvergeRule> page = dao.find(finder, pageRequest);

        return new PageTable<>(page);
    }

    public List<NewMediaConvergeRule> findByCodeAndDelFlag(String code, String delFlag) {
        return dao.findByCodeAndDelFlag(code, delFlag);
    }

    public List<NewMediaConvergeRule> findByDelFlag(String delFlagNormal) {
        return dao.findByDelFlag(delFlagNormal);
    }
}
