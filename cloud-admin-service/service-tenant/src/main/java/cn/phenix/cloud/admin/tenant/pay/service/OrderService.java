package cn.phenix.cloud.admin.tenant.pay.service;

import cn.phenix.cloud.admin.tenant.pay.dao.OrderMapper;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.mall.MallOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Created by wizzer on 2016/12/22.
 */
@Service
@Transactional(readOnly = true)
public class OrderService extends CoreService<OrderMapper, MallOrder> {


    @Autowired

    private OrderMapper orderMapper;

    public PageTable<MallOrder> findPage(DataTableParameter parameter, MallOrder order) {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from MallOrder where 1=1");

        finder.search("orderNo", order.getOrderNo(), Finder.SearchType.LIKE);
        finder.search("subject", order.getSubject(), Finder.SearchType.LIKE);
        finder.search("tenantName", order.getTenantName(), Finder.SearchType.LIKE);
        if(!Strings.isBlank(order.getPayStatus())){
            finder.search("payStatus", Arrays.asList(order.getPayStatus().split(",")), Finder.SearchType.IN);
        }
        finder.append("order by createDate desc");
        Page<MallOrder> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


    @Transactional
    public void updateRefundStatusById(String id){
        orderMapper.updateRefundStatusById(id);
    }

}
