package cn.phenix.cloud.admin.tenant.renter.service;

import cn.phenix.cloud.admin.tenant.renter.dao.TenantUserMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.renter.TenantUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
@Transactional(readOnly = true)
public class TenantUserService extends CoreService<TenantUserMapper,TenantUser>{
    @Autowired
    private TenantUserMapper tenantUserMapper;

    public PageTable<TenantUser> findPage(DataTableParameter parameter, TenantUser tenantUser) {
        PageRequest pageRequest =  parameter.getPageRequest();
        Finder finder = Finder.create("from TenantUser where delFlag=:delFlag")
                .setParam(TenantUser.DEL_FLAG, TenantUser.DEL_FLAG_NORMAL);

        finder.search("tenantName", tenantUser.getTenantName(), Finder.SearchType.LIKE)
                .search("userName", tenantUser.getUserName(), Finder.SearchType.LIKE);


        Page<TenantUser> page = tenantUserMapper.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public TenantUser findOneByLoginname(TenantUser tenantUser) {

        Finder finder = Finder.create("from TenantUser t where t.loginName=:loginName and t.delFlag=:delFlag ")
                .setParam("loginName", tenantUser.getLoginName()).setParam(TenantUser.DEL_FLAG,TenantUser.DEL_FLAG_NORMAL);
       List<TenantUser> list =  tenantUserMapper.find(finder);
       //第二种方式
        // List<TenantUser> list = tenantUserMapper.findByNamedQuery(TenantUserMapper.findOneByLoginname, Parameter.create("loginName",loginName));
        if(list.size() !=0){
            return list.get(0);
        }
        return null;

    }
    public TenantUser findByTenantId(TenantUser tenantUser) {

        Finder finder = Finder.create("from TenantUser t where t.delFlag=:delFlag and tenantId=:tenantId and t.isAdmin = true")
                .setParam(TenantUser.DEL_FLAG,TenantUser.DEL_FLAG_NORMAL).setParam("tenantId", tenantUser.getTenantId());
        List<TenantUser> list =  tenantUserMapper.find(finder);
        if(list.size() !=0){
            return list.get(0);
        }
        return null;

    }

}
