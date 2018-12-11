package cn.phenix.cloud.admin.tenant.pms.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.pms.GuestInfo;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface GuestInfoMapper extends GenericJpaRepository<GuestInfo,String> {

    String findGuestInfo="findGuestInfo";

}
