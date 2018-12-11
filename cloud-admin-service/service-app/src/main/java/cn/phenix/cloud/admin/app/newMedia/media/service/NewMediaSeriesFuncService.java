package cn.phenix.cloud.admin.app.newMedia.media.service;

import cn.phenix.cloud.admin.app.newMedia.media.dao.NewMediaSeriesFuncMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.app.newMedia.NewMediaSeriesFunc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 媒资-功能
 **/
@Service
@Transactional(readOnly = true)
public class NewMediaSeriesFuncService extends CoreService<NewMediaSeriesFuncMapper, NewMediaSeriesFunc> {
}
