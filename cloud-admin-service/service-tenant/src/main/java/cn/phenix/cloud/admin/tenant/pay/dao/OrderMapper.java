package cn.phenix.cloud.admin.tenant.pay.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.mall.MallOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface OrderMapper extends GenericJpaRepository<MallOrder,String> {


    @Modifying
    @Query("update MallOrder set status='6' where id=:id ")
    void updateRefundStatusById(@Param("id") String id);

}
