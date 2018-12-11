package cn.phenix.cloud.admin.app.media.media.service;

import cn.phenix.cloud.admin.app.media.media.dao.MediaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class MediaService {
    @Autowired
    private MediaMapper mediaMapper;

}
