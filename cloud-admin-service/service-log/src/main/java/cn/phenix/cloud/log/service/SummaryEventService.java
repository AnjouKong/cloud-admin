package cn.phenix.cloud.log.service;

import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.log.dao.SummaryEventDao;
import cn.phenix.cloud.vo.log.SummaryEventVo;
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
public class SummaryEventService {
    @Autowired
    private SummaryEventDao summaryEventDao;

    public PageTable<SummaryEventVo> findPage(Pageable pagination, SummaryEventVo summaryEventVo) {
        Page<SummaryEventVo> page = summaryEventDao.findPage(pagination,summaryEventVo);
        return new PageTable<>(page);
    }
    public List<SummaryEventVo> findSum() {
        return summaryEventDao.findSum();
    }

    public SummaryEventVo findById(String id) {
        return summaryEventDao.findById(id);
    }
}
