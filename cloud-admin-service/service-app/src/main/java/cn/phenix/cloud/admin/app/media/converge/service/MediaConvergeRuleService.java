package cn.phenix.cloud.admin.app.media.converge.service;

import cn.phenix.cloud.admin.app.media.converge.dao.MediaConvergeRuleMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaConvergeRule;
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
public class MediaConvergeRuleService extends CoreService<MediaConvergeRuleMapper,MediaConvergeRule>{
    public PageTable<MediaConvergeRule> findPage(DataTableParameter parameter, MediaConvergeRule mediaConvergeRule) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from MediaConvergeRule where delFlag=:delFlag ")
                .setParam("delFlag", MediaConvergeRule.DEL_FLAG_NORMAL);
        finder.search("name",mediaConvergeRule.getName(), Finder.SearchType.LIKE);
        finder.search("code",mediaConvergeRule.getCode(), Finder.SearchType.EQ);

        Page<MediaConvergeRule> page = dao.find(finder,pageRequest);

        return new PageTable<>(page);
    }

    public List<MediaConvergeRule> findByCodeAndDelFlag(String code, String delFlag) {
       return  dao.findByCodeAndDelFlag(code,delFlag);
    }

    public List<MediaConvergeRule> findByDelFlag(String delFlagNormal) {
        return dao.findByDelFlag(delFlagNormal);
    }
}
