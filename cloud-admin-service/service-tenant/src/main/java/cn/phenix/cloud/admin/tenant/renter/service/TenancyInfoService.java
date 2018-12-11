package cn.phenix.cloud.admin.tenant.renter.service;

import cn.phenix.cloud.admin.tenant.renter.dao.TenancyInfoMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.tenant.renter.TenancyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
@Transactional(readOnly = true)
public class TenancyInfoService extends CoreService<TenancyInfoMapper,TenancyInfo>{

    @Autowired
    private TenancyInfoMapper tenancyInfoMapper;


    public List<TenancyInfo> findByAppIdOrAppSecret(String appId, String appSecret) {
        return tenancyInfoMapper.findByAppIdOrAppSecret(appId,appSecret);
    }
}
