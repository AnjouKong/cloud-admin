package cn.phenix.cloud.admin.tenant.renter.service;

import cn.phenix.cloud.admin.tenant.renter.dao.TenantSceneResourceMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.model.tenant.renter.TenantSceneResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by fxq
 */
@Service
@Transactional(readOnly = true)
public class TenantSceneResourceService extends CoreService<TenantSceneResourceMapper, TenantSceneResource> {


    @Transactional
    public void saveAdvList(List<TenantSceneResource> tenantSceneResourceList) {
        dao.saveAll(tenantSceneResourceList);
    }

    public List<TenantSceneResource> findByTenantSceneIdAndDelFlag(String tenantSceneId) {
        Finder finder = Finder.create("select r from TenantSceneResource r , CmsResource cms ");
        finder.whereExcludeDel("r");
        finder.append(" and r.resourceId=cms.id ");
        finder.search("r.tenantSceneId", tenantSceneId);
        finder.search("cms.disabled", false);
        finder.search("cms.delFlag", "0");

        finder.append(" order by r.index");
        List<TenantSceneResource> advList = dao.find(finder);

        return advList;
    }


    @Transactional
    public void updateByTenantSceneId(String tenantSceneId) {
        Finder finder = Finder.create("from TenantSceneResource ");
        finder.whereExcludeDel();
        finder.search("tenantSceneId", tenantSceneId);
        List<TenantSceneResource> advList = dao.find(finder);
        for (TenantSceneResource s : advList) {
            deleteById(s.getId());
        }
    }

    @Transactional
    public void deleteByTenantSceneId(String tenantSceneId) {
        Finder finder = Finder.create("from TenantSceneResource ");
        finder.whereExcludeDel();
        finder.search("tenantSceneId", tenantSceneId);
        List<TenantSceneResource> advList = dao.find(finder);
        dao.deleteAll(advList);
    }

    @Transactional
    public void deleteByTenantSceneIdAndSucceed(String tenantSceneId) {
        Finder finder = Finder.create("from TenantSceneResource ");
        finder.whereExcludeDel();
        finder.search("tenantSceneId", tenantSceneId);
        finder.search("succeed", true);
        List<TenantSceneResource> advList = dao.find(finder);
        dao.deleteAll(advList);
    }
}
