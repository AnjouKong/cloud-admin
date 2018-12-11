package cn.phenix.cloud.admin.tenant.renter.service;

import cn.phenix.cloud.admin.tenant.renter.dao.TenancyMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.sys.SysImplementUserTenant;
import cn.phenix.model.tenant.renter.Tenancy;
import cn.phenix.model.tenant.renter.TenancyInfo;
import org.apache.commons.lang3.StringUtils;
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
public class TenancyService extends CoreService<TenancyMapper, Tenancy> {

    public Tenancy findByTenancyCode(String tenancyCode) {
        return dao.findByTenancyCodeAndDelFlag(tenancyCode, Tenancy.DEL_FLAG_NORMAL);
    }


    public PageTable<Tenancy> findPage(DataTableParameter parameter, Tenancy tenancy, TenancyInfo tenancyInfo) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select a from Tenancy a left join TenancyInfo b on a.id=b.id where a.delFlag=:delFlag")
                .setParam(Tenancy.DEL_FLAG, Tenancy.DEL_FLAG_NORMAL);

        finder.search("a.tenancyName", tenancy.getTenancyName(), Finder.SearchType.LIKE);
        finder.search("a.tenancyCode", tenancy.getTenancyCode(), Finder.SearchType.LIKE);
        if (StringUtils.isNoneEmpty(tenancyInfo.getAppSwitch())) {
            finder.append("and (b.appSwitch = :appSwitch OR b.appSwitch IS NULL)").setParam("appSwitch", tenancyInfo.getAppSwitch());//系统升级开关
        }
        finder.append(" order by a.tenancyCode asc,a.updateDate desc");
        Page<Tenancy> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<Tenancy> findByStatus(String s) {
        Finder finder = Finder.create("from Tenancy ");
        finder.whereExcludeDel();
        finder.search("status", s);
        return dao.find(finder);
    }

    public List<Tenancy> findTenancyList(String userId) {
        Finder finder = Finder.create("select t from Tenancy t,SysImplementUserTenant s where s.delFlag=:delFlag and t.delFlag=:delFlagT and s.userId=:userId and s.tenantId=t.id")
                .setParam("delFlag", SysImplementUserTenant.DEL_FLAG_NORMAL).setParam("delFlagT", Tenancy.DEL_FLAG_NORMAL).setParam("userId", userId);
        return dao.find(finder);
    }

    public List<Tenancy> findByLevelId(String levelId) {
        Finder finder = Finder.create("select t from Tenancy t where t.delFlag=:delFlag  and t.levelId=:levelId")
                .setParam("delFlag", Tenancy.DEL_FLAG_NORMAL).setParam("levelId", levelId);
        return dao.find(finder);
    }

    public List<Tenancy> findByDelFlag(String delFlag) {
        return dao.findByDelFlag(delFlag);
    }

    public List<Tenancy> findByIdIn(String[] tenantId) {
        return dao.findByIdIn(tenantId);
    }

    public List<Tenancy> getTenantInfoByPackageId(String packageId) {
        Finder finder = Finder.create("select t from Tenancy t left join PackageTenant b on t.id=b.tenantId where t.delFlag='0' and  b.packageId=:packageId")
                .setParam("packageId", packageId);
        return dao.find(finder);
    }
}
