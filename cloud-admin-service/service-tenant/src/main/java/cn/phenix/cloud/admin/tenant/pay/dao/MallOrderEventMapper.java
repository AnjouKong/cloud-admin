package cn.phenix.cloud.admin.tenant.pay.dao;


import cn.phenix.cloud.jpa.GenericJpaRepository;
import cn.phenix.model.mall.MallOrder;
import cn.phenix.model.mall.MallOrderEvent;
import cn.phenix.model.tenant.device.DeviceUserExtends;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface MallOrderEventMapper extends GenericJpaRepository<MallOrderEvent,String> {

    List<MallOrderEvent> findByOrderNoOrderByCreateDateDesc(String orderNo);
}
