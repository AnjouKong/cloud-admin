package cn.phenix.cloud.admin.tenant.test.service;

import cn.phenix.cloud.admin.tenant.test.dao.TestGroupSwitchConfigMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.model.tenant.test.TestGroupSwitchConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq on 2018/5/22.
 */
@Service
@Transactional(readOnly = true)
public class TestGroupSwitchConfigService extends CoreService<TestGroupSwitchConfigMapper, TestGroupSwitchConfig> {


    public void deleteByGroupId(String groupId) {
        Finder finder = Finder.create("from TestGroupSwitchConfig where groupId=:groupId").setParam("groupId", groupId);
        dao.deleteAll(dao.find(finder));
    }

    public List<TestGroupSwitchConfig> findByGroupId(String groupId) {
        Finder finder = Finder.create("from TestGroupSwitchConfig where groupId=:groupId").setParam("groupId", groupId);
        return dao.find(finder);
    }
}
