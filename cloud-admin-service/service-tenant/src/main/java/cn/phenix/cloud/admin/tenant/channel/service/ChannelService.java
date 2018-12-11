package cn.phenix.cloud.admin.tenant.channel.service;

import cn.phenix.cloud.admin.tenant.channel.dao.ChannelMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.tenant.channel.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by
 */
@Service
@Transactional(readOnly = true)
public class ChannelService extends CoreService<ChannelMapper, Channel> {


    public PageTable<Channel> findPage(DataTableParameter parameter, Channel channel) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("from Channel ");
        finder.whereExcludeDel();
        finder.search("tenantId", channel.getTenantId());
        finder.search("name", channel.getName(), Finder.SearchType.LIKE);

        finder.append(" order by number");
        Page<Channel> page = dao.find(finder, pageRequest);
        return new PageTable<>(page);
    }


}
