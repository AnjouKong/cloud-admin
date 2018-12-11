package cn.phenix.cloud.admin.tenant.renter.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.renter.TenancyInfo;

import java.util.List;

public interface TenancyInfoMapper extends GenericJpaRepository<TenancyInfo,String> {

    List<TenancyInfo> findByAppIdOrAppSecret(String appId, String appSecret);
}
