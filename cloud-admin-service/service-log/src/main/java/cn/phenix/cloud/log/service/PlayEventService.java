package cn.phenix.cloud.log.service;

import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.log.dao.PlayEventDao;
import cn.phenix.cloud.vo.log.LogDeviceEventV1Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mgm
 **/
@Service
@Transactional(readOnly = true)
public class PlayEventService {
    @Autowired
    private PlayEventDao playEventDao;

    public PageTable<LogDeviceEventV1Vo> findPage(Pageable pagination, LogDeviceEventV1Vo eventV1Vo) {
        Page<LogDeviceEventV1Vo> page = playEventDao.findPage(pagination,eventV1Vo);
        return new PageTable<>(page);
    }

    public List<LogDeviceEventV1Vo> findTop10() {
        return playEventDao.findTop10();
    }
}
