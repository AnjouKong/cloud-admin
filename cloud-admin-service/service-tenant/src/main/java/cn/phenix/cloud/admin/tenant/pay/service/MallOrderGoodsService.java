package cn.phenix.cloud.admin.tenant.pay.service;

import cn.phenix.cloud.admin.tenant.pay.dao.MallOrderGoodsMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.mall.MallOrderGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * mgm
 */
@Service
@Transactional(readOnly = true)
public class MallOrderGoodsService extends CoreService<MallOrderGoodsMapper, MallOrderGoods> {


    public PageTable<MallOrderGoods> findPage(DataTableParameter parameter, String  orderId) {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("from MallOrderGoods where 1=1");
        finder.search("orderId", orderId, Finder.SearchType.EQ);
        finder.append("order by createDate desc");
        Page<MallOrderGoods> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }

}
