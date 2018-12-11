package cn.phenix.cloud.admin.tenant.device.service;


import cn.phenix.cloud.admin.tenant.device.dao.DeviceUserExtendMapper;
import cn.phenix.cloud.admin.tenant.device.dao.DeviceUserMapper;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.device.DeviceUser;
import cn.phenix.model.tenant.device.DeviceUserExtends;
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
public class DeviceUserService extends CoreService<DeviceUserMapper, DeviceUser> {
    @Autowired
    private DeviceUserMapper deviceUserMapper;
    @Autowired
    private DeviceUserExtendMapper deviceUserExtendMapper;


    public PageTable<DeviceUser> findDeviceUserByPage(DataTableParameter parameter, DeviceUser deviceUser) {

        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select a from DeviceUser a ,Tenancy b  where a.delFlag=:delFlag and a.tenantId= b.id").setParam(DeviceUser.DEL_FLAG, DeviceUser.DEL_FLAG_NORMAL);

        finder.search("a.roomId", deviceUser.getRoomId(), Finder.SearchType.EQ)
                .search("a.deviceSystem", deviceUser.getDeviceSystem(), Finder.SearchType.LIKE)
                .search("a.deviceId", deviceUser.getDeviceId(), Finder.SearchType.LIKE);
        finder.search("a.tenantId", deviceUser.getTenantId());
        finder.search("a.deviceGroupCode", deviceUser.getDeviceGroupCode());
        finder.search("b.tenancyName", deviceUser.getTenantName(), Finder.SearchType.LIKE);
        //新添测试组
        finder.search("a.testGroupId", deviceUser.getTestGroupId());
        finder.append(" order by roomId ");
        Page<DeviceUser> page = deviceUserMapper.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public PageTable<DeviceUser> findForTestGroup(DataTableParameter parameter, DeviceUser deviceUser) {

        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select a from DeviceUser a ,Tenancy b  where a.delFlag=:delFlag and a.tenantId= b.id").setParam(DeviceUser.DEL_FLAG, DeviceUser.DEL_FLAG_NORMAL);

//        finder.search("a.roomId", deviceUser.getRoomId(), Finder.SearchType.EQ)
//                .search("a.deviceId", deviceUser.getDeviceId(), Finder.SearchType.LIKE);

        finder.search("a.tenantId", deviceUser.getTenantId());

        if (!Strings.isBlank(deviceUser.getTestGroupId())) {
            finder.append(" and (a.testGroupId is null or a.testGroupId ='' ) ");
        }

        if (!Strings.isBlank(deviceUser.getRoomIdOrDeviceId())) {
            finder.append(" and (a.roomId=:roomId or a.deviceId like :deviceId ) ")
                    .setParam("roomId", deviceUser.getRoomIdOrDeviceId()).setParam("deviceId", "%" + deviceUser.getRoomIdOrDeviceId() + "%");
        }

        Page<DeviceUser> page = deviceUserMapper.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    public List<DeviceUser> findByDeviceId(String deviceId) {
        return deviceUserMapper.findByDeviceIdAndDelFlag(deviceId, "0");
    }

    public List<DeviceUser> findByTenantIdAndDelFlag(String tenantId) {
        return dao.findByTenantIdAndDelFlag(tenantId, "0");
    }


    public List<DeviceUser> findByDeviceGroupCodeAndDelFlag(String deviceGroupCode) {
        return dao.findByDeviceGroupCodeAndDelFlag(deviceGroupCode, "0");
    }


    @Transactional
    public void logicDelById(String id, String s) {
        deviceUserMapper.updateDelFlag(id, s);

    }

    @Transactional
    public void logicDelByIds(String[] ids, String s) {

        for (String id : ids) {
            deviceUserMapper.updateDelFlag(id, s);
        }

    }

    @Transactional
    public void updateByPrimaryKeySelective(DeviceUser user) {
        deviceUserMapper.save(user);
    }

    @Transactional
    public void updateWithExtends(DeviceUser user, DeviceUserExtends deviceUserExtends) {
        deviceUserMapper.save(user);
        if (StringUtils.isNoneBlank(deviceUserExtends.getSpecificationId())) {
            deviceUserExtendMapper.save(deviceUserExtends);
        }
    }

    @Transactional
    public void removeGroup(String id) {
        dao.removeGroup(id);
    }

    @Transactional
    public void updateTestGroup(String testGroupId, String[] id) {
        dao.updateTestGroup(testGroupId, id);
    }
}
