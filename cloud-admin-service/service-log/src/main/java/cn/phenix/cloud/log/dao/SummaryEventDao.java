package cn.phenix.cloud.log.dao;


import cn.phenix.cloud.core.jdbc.JdbcFinder;
import cn.phenix.cloud.core.jdbc.MyJdbcTemplate;
import cn.phenix.cloud.vo.log.SummaryEventVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒店日统计
 *
 * @author mgm
 **/
@Repository
public class SummaryEventDao {

    @Autowired
    @Qualifier("logJdbcTemplate")
    private MyJdbcTemplate logJdbcTemplate;


    public Page<SummaryEventVo> findPage(Pageable pagination, SummaryEventVo summaryEventVo) {
        JdbcFinder finder = JdbcFinder.create("select * from log_device_summary_day a where 1=1");
        finder.search("a.createDay", summaryEventVo.getScreateDay(), JdbcFinder.SearchType.GE);
        finder.search("a.createDay", summaryEventVo.getEcreateDay(), JdbcFinder.SearchType.LE);
        finder.search("a.tenantName", summaryEventVo.getTenantName(), JdbcFinder.SearchType.LIKE);
        finder.append(" order by a.createDay desc,a.id asc");
        Page<SummaryEventVo> page = logJdbcTemplate.queryForPage(finder, pagination, new BeanPropertyRowMapper(SummaryEventVo.class));
        return page;
    }

    public List<SummaryEventVo> findSum() {
        String sql = "SELECT SUM(a.`loginNum`) AS loginNum, SUM(a.`orderNum`) AS orderNum, SUM(a.`deviceNum`) AS deviceNum,SUM(a.`playDeviceNum`) AS playDeviceNum,SUM(a.`playNum`) AS playNum,SUM(a.`roomNum`) AS roomNum,SUM(a.`paidNum`) AS paidNum,MAX(a.`createDay`) AS createDay ,SUM(a.`sumPrice`) AS sumPrice FROM `log_device_summary_day` a GROUP BY a.`createDay` ORDER BY a.`createDay` DESC,a.id asc limit 7";
        //List<SumaryEventVo> top10 = logJdbcTemplate.queryForListBean(sql,null,SumaryEventVo.class);
        return logJdbcTemplate.queryForListBean(sql,null,SummaryEventVo.class);
    }

    public SummaryEventVo findById(String id) {
        //JdbcFinder finder = JdbcFinder.create("select * from log_device_summary_day a where 1=1 and id=:id").setParam("id",id);
        String sql = "select * from log_device_summary_day a where id='"+id+"'";
        return logJdbcTemplate.queryForBean(sql,null,SummaryEventVo.class);
    }
}

