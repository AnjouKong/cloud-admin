package cn.phenix.cloud.admin.tenant.renter.service;

import cn.phenix.cloud.admin.tenant.renter.dao.TenantSceneRecordMapper;
import cn.phenix.cloud.core.utils.DateUtils;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.renter.TenantSceneRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Date;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class TenantSceneRecordService extends CoreService<TenantSceneRecordMapper, TenantSceneRecord> {

    @Autowired
    private TenantSceneService tenantSceneService;

    public PageTable<TenantSceneRecord> findByGroupId(DataTableParameter parameter, TenantSceneRecord tenantSceneRecord) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create(" from TenantSceneRecord ");
        finder.whereExcludeDel();
        finder.search("groupId", tenantSceneRecord.getGroupId());
        finder.append(" order by version desc");
        Page<TenantSceneRecord> recordList = dao.find(finder, pageRequest);
        for (TenantSceneRecord record : recordList.getContent()) {
            record.setSceneName(tenantSceneService.get(record.getTenantSceneId()).getSceneName());
        }
        return new PageTable<>(recordList);
    }

    @Transient
    public TenantSceneRecord save(TenantSceneRecord tenantSceneRecord) {
        Date date = new Date();
        tenantSceneRecord.setVersion(DateUtils.format(date, "yyyyMMddHHmmss"));
        return dao.save(tenantSceneRecord);
    }

}
