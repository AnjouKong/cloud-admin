package cn.phenix.cloud.admin.tenant.pay.service;

import cn.phenix.cloud.admin.tenant.pay.dao.MallOrderEventMapper;
import cn.phenix.cloud.admin.tenant.pay.dao.OrderMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.mall.MallOrder;
import cn.phenix.model.mall.MallOrderEvent;
import cn.phenix.model.tenant.device.DeviceUserExtends;
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
public class MallOrderEventService extends CoreService<MallOrderEventMapper, MallOrderEvent> {

    public List<MallOrderEvent> findByOrderNoOrderByCreateDateDesc(String orderNo) {

        return dao.findByOrderNoOrderByCreateDateDesc(orderNo);
    }


}
