package cn.phenix.cloud.admin.app.log.mediaChange.service;

import cn.phenix.cloud.admin.app.log.mediaChange.dao.MediaPullLogMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.log.MediaPullLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mgm
 **/
@Service
@Transactional(readOnly = true)
public class MediaPullLogService extends CoreService<MediaPullLogMapper, MediaPullLog> {
    @Autowired
    private MediaPullLogMapper mediaPullLogMapper;

    public PageTable<MediaPullLog> findPage(DataTableParameter parameter, MediaPullLog mediaPullLog, String sstartDate, String estartDate) throws ParseException {
        PageRequest pageRequest = parameter.getPageRequest();

        Finder finder = Finder.create("select m from MediaPullLog m where 1=1 ");
        if(StringUtils.isNotEmpty(sstartDate)) {
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
            Date date = sdf.parse(sstartDate);
            finder.search("startDate", date, Finder.SearchType.GE);
        }
        if(StringUtils.isNotEmpty(estartDate)) {
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );//小写的mm表示的是分钟
            Date date = sdf.parse(estartDate);
            finder.search("startDate", date, Finder.SearchType.LE);
        }
        finder.append("order by startDate desc");
        Page<MediaPullLog> page = mediaPullLogMapper.find(finder,pageRequest);
        return new PageTable<>(page);
    }
}
