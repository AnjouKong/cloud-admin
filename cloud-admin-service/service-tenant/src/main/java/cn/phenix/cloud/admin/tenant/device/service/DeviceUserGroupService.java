package cn.phenix.cloud.admin.tenant.device.service;


import cn.phenix.cloud.admin.tenant.device.dao.DeviceUserGroupMapper;
import cn.phenix.cloud.admin.tenant.renter.dao.TenancyMapper;
import cn.phenix.cloud.admin.tenant.renter.dao.TenantSceneMapper;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.device.DeviceUser;
import cn.phenix.model.tenant.device.DeviceUserGroup;
import cn.phenix.model.tenant.renter.Tenancy;
import cn.phenix.model.tenant.renter.TenantScene;
import org.apache.commons.lang3.StringUtils;
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
public class DeviceUserGroupService extends CoreService<DeviceUserGroupMapper, DeviceUserGroup> {
    @Autowired
    private DeviceUserGroupMapper deviceUserGroupMapper;
    @Autowired
    private TenancyMapper tenancyMapper;
    @Autowired
    private TenantSceneMapper tenantSceneMapper;


    public PageTable<DeviceUserGroup> findDeviceUserGroupByPage(DataTableParameter parameter, DeviceUserGroup deviceUserGroup, String tenancyName) {

        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select a from DeviceUserGroup a left join Tenancy b on a.tenantId = b.id where a.delFlag=:delFlag and b.id=:tenantId ")
                .setParam(DeviceUser.DEL_FLAG, DeviceUser.DEL_FLAG_NORMAL)
                .setParam("tenantId", deviceUserGroup.getTenantId());

        finder.search("b.tenancyName", tenancyName, Finder.SearchType.LIKE);
        finder.search("a.groupName", deviceUserGroup.getGroupName(), Finder.SearchType.LIKE);
        Page<DeviceUserGroup> page = deviceUserGroupMapper.find(finder, pageRequest);
        //显示商户名
        for (int i = 0; i < page.getContent().size(); i++) {
            Tenancy tenancy = tenancyMapper.findById(page.getContent().get(i).getTenantId()).orElse(null);
            if (!Strings.isBlank(page.getContent().get(i).getSceneId())) {
                TenantScene scene = tenantSceneMapper.findById(page.getContent().get(i).getSceneId()).orElse(null);
                page.getContent().get(i).setSceneName(scene == null || scene.getSceneName() == null ? null : scene.getSceneName());
            }
            page.getContent().get(i).setTenantName(tenancy.getTenancyName());
        }
        return new PageTable<>(page);
    }

    public PageTable<DeviceUserGroup> findAllDeviceUserGroupByPage(DataTableParameter parameter, DeviceUserGroup deviceUserGroup, String tenancyName) {

        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select a from DeviceUserGroup a left join Tenancy b on a.tenantId = b.id where a.delFlag=:delFlag ")
                .setParam(DeviceUser.DEL_FLAG, DeviceUser.DEL_FLAG_NORMAL);

        finder.search("b.tenancyName", tenancyName, Finder.SearchType.LIKE);
        finder.search("a.groupName", deviceUserGroup.getGroupName(), Finder.SearchType.LIKE);
        finder.search("a.tenantId", deviceUserGroup.getTenantId(), Finder.SearchType.EQ);
        Page<DeviceUserGroup> page = deviceUserGroupMapper.find(finder, pageRequest);
        //显示商户名
        for (int i = 0; i < page.getContent().size(); i++) {
            Tenancy tenancy = tenancyMapper.findById(page.getContent().get(i).getTenantId()).orElse(null);
            if (tenancy != null) {
                if (StringUtils.isNotEmpty(tenancy.getTenancyName())) {
                    page.getContent().get(i).setTenantId(tenancy.getTenancyName());
                }
            }
        }
        return new PageTable<>(page);
    }

    public List<DeviceUserGroup> findByDelFlagAndTenantId(String delFlag, String id) {
        List<DeviceUserGroup> list = dao.findByDelFlagAndTenantId(delFlag, id);

        //显示商户名
        for (int i = 0; i < list.size(); i++) {
            if (!Strings.isBlank(list.get(i).getSceneId())) {
                TenantScene scene = tenantSceneMapper.findById(list.get(i).getSceneId()).orElse(null);
                list.get(i).setSceneName(scene == null || scene.getSceneName() == null ? null : scene.getSceneName());
            }
        }
        return list;
    }

    public List<DeviceUserGroup> findByTenantSceneId(String tenantSceneId) {
        Finder finder = Finder.create("from DeviceUserGroup");
        finder.whereExcludeDel();
        finder.search("sceneId", tenantSceneId);
        return dao.find(finder);
    }

    public List<DeviceUserGroup> findByDelFlag(String s) {
        return dao.findByDelFlag(s);
    }
}
