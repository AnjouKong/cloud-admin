package cn.phenix.cloud.admin.tenant.test.service;

import cn.phenix.cloud.admin.tenant.test.dao.TestSwitchMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.test.TestSwitch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq on 2018/5/22.
 */
@Service
@Transactional(readOnly = true)
public class TestSwitchService extends CoreService<TestSwitchMapper, TestSwitch> {


    public PageTable<TestSwitch> findPage(DataTableParameter parameter, TestSwitch testSwitch) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from TestSwitch ");
        finder.whereExcludeDel();
        finder.search("name", testSwitch.getName(), Finder.SearchType.LIKE);

        finder.append(" order by updateDate desc");
        Page<TestSwitch> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<TestSwitch> findByCode(String code) {
        Finder finder = Finder.create("from TestSwitch ");
        finder.whereExcludeDel();
        finder.search("code", code);
        return dao.find(finder);
    }

    public List<TestSwitch> findAll() {
        Finder finder = Finder.create("from TestSwitch ");
        finder.whereExcludeDel();
        finder.append(" order by updateDate desc");
        return dao.find(finder);
    }


}
