package cn.phenix.cloud.log.dao;


import cn.phenix.cloud.core.jdbc.JdbcFinder;
import cn.phenix.cloud.core.jdbc.MyJdbcTemplate;
import cn.phenix.cloud.vo.log.LogDeviceEventV1Vo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * 去重事件
 **/
@Repository
public class LoginEventDao {

    @Autowired
    @Qualifier("logJdbcTemplate")
    private MyJdbcTemplate logJdbcTemplate;


    public Page<LogDeviceEventV1Vo> findPage(Pageable pagination, LogDeviceEventV1Vo eventV1Vo) {
        JdbcFinder finder = JdbcFinder.create("select * from log_device_login a where 1=1");
        finder.search("a.eventCode", eventV1Vo.getEventCode().toUpperCase(), JdbcFinder.SearchType.EQ);
        if(StringUtils.isNotBlank(eventV1Vo.getSstartTime())){
            finder.search("a.startTime", eventV1Vo.getSstartTime()+" 00:00:00 0000", JdbcFinder.SearchType.GE);
            finder.search("a.startTime", eventV1Vo.getSstartTime()+" 23:59:59 9999", JdbcFinder.SearchType.LE);
        }
        if(StringUtils.isNotBlank(eventV1Vo.getScreateDay())){
            finder.search("a.createDay", eventV1Vo.getScreateDay(), JdbcFinder.SearchType.GE);
        }
        if(StringUtils.isNotBlank(eventV1Vo.getEcreateDay())){
            finder.search("a.createDay", eventV1Vo.getEcreateDay(), JdbcFinder.SearchType.LE);
        }
        finder.search("a.tenantName", eventV1Vo.getTenantName(), JdbcFinder.SearchType.LIKE);
        finder.append(" order by a.createDay desc,a.id asc");
        Page<LogDeviceEventV1Vo> page = logJdbcTemplate.queryForPage(finder, pagination, new BeanPropertyRowMapper(LogDeviceEventV1Vo.class));
        return page;
    }

}
