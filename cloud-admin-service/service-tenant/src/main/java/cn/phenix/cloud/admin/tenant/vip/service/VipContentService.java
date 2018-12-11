package cn.phenix.cloud.admin.tenant.vip.service;

import cn.phenix.cloud.admin.tenant.vip.dao.VipContentMapper;
import cn.phenix.cloud.admin.tenant.vip.dao.VipPackageMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.vip.VipContent;
import cn.phenix.model.tenant.vip.VipContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author socilents
 * Create in 2018/5/24 14:02
 **/
@Service
@Transactional(readOnly = true)
public class VipContentService extends CoreService<VipContentMapper,VipContent> {
    @Autowired
    private VipContentMapper vipContentMapper;

    public PageTable<VipContent> findPage(DataTableParameter parameter, VipContent vipContent,String packageId) {
        PageRequest pageRequest =  parameter.getPageRequest();
        Finder finder = Finder.create("select a from VipContent a where a.delFlag=:delFlag and vipPackage.id =:packageId")
                .setParam("delFlag",VipContent.DEL_FLAG_NORMAL).setParam("packageId",packageId);

        finder.search("a.contentName", vipContent.getContentName(), Finder.SearchType.LIKE);
        finder.append(" order by a.updateDate desc");
        Page<VipContent> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    @Transactional
    public void deleteByPackageIds(String[] ids) {
        for(String id:ids){
            vipContentMapper.deleteByPackageId(id);
        }
    }

    public List<VipContent> findByPackageId(String id) {
        return vipContentMapper.findByPackageId(id);
    }

    @Transactional
    public void updateByPackageId(String id) {
        vipContentMapper.updateByPackageId(id);
    }
}
