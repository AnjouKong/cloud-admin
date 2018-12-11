package cn.phenix.cloud.admin.tenant.vip.service;

import cn.phenix.cloud.admin.tenant.vip.dao.VipPackageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.vip.VipPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author socilents
 * Create in 2018/5/24 14:02
 **/
@Service
@Transactional(readOnly = true)
public class VipPackageService extends CoreService<VipPackageMapper,VipPackage> {
    public PageTable<VipPackage> findPage(DataTableParameter parameter, VipPackage vipPackage) {
        PageRequest pageRequest =  parameter.getPageRequest();
        Finder finder = Finder.create("select a from VipPackage a where a.delFlag=:delFlag")
                .setParam("delFlag",VipPackage.DEL_FLAG_NORMAL);

        finder.search("a.packageName", vipPackage.getPackageName(), Finder.SearchType.LIKE);
        finder.append(" order by a.updateDate desc");
        Page<VipPackage> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }
}
