package cn.phenix.cloud.admin.tenant.pms.service;

import cn.phenix.cloud.admin.tenant.pms.dao.GuestInfoMapper;
import cn.phenix.cloud.admin.tenant.pms.vo.GuestInfoVo;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.tenant.pms.GuestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
@Transactional(readOnly = true)
public class GuestInfoService extends CoreService<GuestInfoMapper, GuestInfo> {


    @Autowired
    private GuestInfoMapper guestInfoMapper;

    public PageTable<GuestInfoVo> findPage(DataTableParameter parameter, GuestInfoVo guestInfoVo) {
        PageRequest pageRequest = parameter.getPageRequest();
        Parameter p=Parameter.create(GuestInfoMapper.findGuestInfo);
        if(!Strings.isBlank(guestInfoVo.getTenantName())){
            p.insert("tenancyName","%"+guestInfoVo.getTenantName()+"%");
        }

        if(!Strings.isBlank(guestInfoVo.getGuestName())){
            p.insert("guestName","%"+guestInfoVo.getGuestName()+"%");
        }
        Page<GuestInfoVo> page = dao.findPageByNamedQuery(p,pageRequest, GuestInfoVo.class);

        return new PageTable<>(page);
    }



}
