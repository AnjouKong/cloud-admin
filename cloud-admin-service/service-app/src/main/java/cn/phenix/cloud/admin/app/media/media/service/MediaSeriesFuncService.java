package cn.phenix.cloud.admin.app.media.media.service;

import cn.phenix.cloud.admin.app.media.media.dao.MediaSeriesFuncMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.app.media.MediaSeriesFunc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 媒资-功能
 **/
@Service
@Transactional(readOnly = true)
public class MediaSeriesFuncService extends CoreService<MediaSeriesFuncMapper, MediaSeriesFunc> {
}
