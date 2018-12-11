package cn.phenix.cloud.admin.tenant.test.service;

import cn.phenix.cloud.admin.tenant.renter.dao.TenancyMapper;
import cn.phenix.cloud.admin.tenant.test.dao.TestGroupMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.renter.Tenancy;
import cn.phenix.model.tenant.test.TestGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fxq on 2018/5/22.
 */
@Service
@Transactional(readOnly = true)
public class TestGroupService extends CoreService<TestGroupMapper, TestGroup> {

    @Autowired
    private TenancyMapper tenancyMapper;

    public PageTable<TestGroup> findPage(DataTableParameter parameter, TestGroup testGroup) {

        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select a from TestGroup a left join Tenancy b on a.tenantId = b.id where a.delFlag=:delFlag ")
                .setParam(TestGroup.DEL_FLAG, TestGroup.DEL_FLAG_NORMAL);

        finder.search("a.tenantId", testGroup.getTenantId());
        finder.search("b.tenancyName", testGroup.getTenantName(), Finder.SearchType.LIKE);
        finder.search("a.groupName", testGroup.getGroupName(), Finder.SearchType.LIKE);
        finder.append(" order by a.updateDate desc");
        Page<TestGroup> page = dao.find(finder, pageRequest);
        //显示商户名
        for (int i = 0; i < page.getContent().size(); i++) {
            Tenancy tenancy = tenancyMapper.findById(page.getContent().get(i).getTenantId()).orElse(null);
            page.getContent().get(i).setTenantName(tenancy.getTenancyName());
        }
        return new PageTable<>(page);
    }


}
