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

import java.util.List;

/**
 * 终端事件
 *
 * @author mgm
 **/
@Repository
public class PlayEventDao {

    @Autowired
    @Qualifier("logJdbcTemplate")
    private MyJdbcTemplate logJdbcTemplate;


    public Page<LogDeviceEventV1Vo> findPage(Pageable pagination, LogDeviceEventV1Vo eventV1Vo) {
        JdbcFinder finder = JdbcFinder.create("select * from log_device_play a where 1=1");
        finder.search("a.eventCode", eventV1Vo.getEventCode().toUpperCase(), JdbcFinder.SearchType.EQ);
        if (StringUtils.isNotBlank(eventV1Vo.getSstartTime())) {
            finder.search("a.startTime", eventV1Vo.getSstartTime() + " 00:00:00 0000", JdbcFinder.SearchType.GE);
            finder.search("a.startTime", eventV1Vo.getSstartTime() + " 23:59:59 9999", JdbcFinder.SearchType.LE);
        }
        finder.search("a.tenantName", eventV1Vo.getTenantName(), JdbcFinder.SearchType.LIKE);
        finder.append(" order by a.createDay desc,a.id asc");
        Page<LogDeviceEventV1Vo> page = logJdbcTemplate.queryForPage(finder, pagination, new BeanPropertyRowMapper(LogDeviceEventV1Vo.class));
        return page;
    }

    public List<LogDeviceEventV1Vo> findTop10() {
        String sql = "SELECT a.*,COUNT(resourceId) AS num FROM `log_device_event` a WHERE a.`createDay` = DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 1 DAY),'%Y-%m-%d')";
                sql+="  AND a.`eventCode`='MEDIA_DETAIL_EVENT' and a.`resourceId` !='' and a.`resourceName` != '' GROUP BY a.resourceId ORDER BY COUNT(resourceId) DESC,a.id asc LIMIT 10 ";
        System.out.println("top10:"+sql);
        List<LogDeviceEventV1Vo> top10 = logJdbcTemplate.queryForListBean(sql,null,LogDeviceEventV1Vo.class );
        return top10;
    }
}

