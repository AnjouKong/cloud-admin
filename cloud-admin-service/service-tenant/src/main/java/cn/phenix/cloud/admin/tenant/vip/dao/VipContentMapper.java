package cn.phenix.cloud.admin.tenant.vip.dao;

import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.tenant.vip.VipContent;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author socilents
 * Create in 2018/5/24 14:03
 **/
public interface VipContentMapper extends GenericJpaRepository<VipContent,String> {

    @Modifying
    @Query("update VipContent set delFlag='1' where vipPackage.id=:packageId")
    void deleteByPackageId(@Param("packageId") String packageId);

    @Query("select a from VipContent a where a.vipPackage.id= :packageId")
    List<VipContent> findByPackageId(@Param("packageId")String packageId);

    @Modifying
    @Query("update VipContent set isRecommend='0' where vipPackage.id=:packageId")
    void updateByPackageId(@Param("packageId")String packageId);
}
