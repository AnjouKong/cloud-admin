package cn.phenix.cloud.log.service;

import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.log.dao.LoginEventDao;
import cn.phenix.cloud.vo.log.LogDeviceEventV1Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mgm
 **/
@Service
@Transactional(readOnly = true)
public class LoginEventService {
    @Autowired
    private LoginEventDao loginEventDao;

    public PageTable<LogDeviceEventV1Vo> findPage(Pageable pagination, LogDeviceEventV1Vo eventV1Vo) {
        Page<LogDeviceEventV1Vo> page = loginEventDao.findPage(pagination,eventV1Vo);
        return new PageTable<>(page);
    }
}
