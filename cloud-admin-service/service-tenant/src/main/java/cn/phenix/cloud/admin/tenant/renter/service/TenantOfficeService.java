package cn.phenix.cloud.admin.tenant.renter.service;

import cn.phenix.cloud.admin.tenant.renter.dao.TenantOfficeMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.tenant.renter.TenantOffice;
import cn.phenix.model.tenant.renter.TenantUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mgm
 */
@Service
@Transactional(readOnly = true)
public class TenantOfficeService extends CoreService<TenantOfficeMapper,TenantOffice>{

    public TenantOffice find4Add(TenantUser tenancy) {

        Parameter p=Parameter.create();
        p.insert("tenantId",tenancy.getTenantId());
        List<TenantOffice> officeList = dao.findByNamedQuery(TenantOfficeMapper.find4Add,p,TenantOffice.class);

        return officeList.size()==0?null:officeList.get(0);
    }
}
