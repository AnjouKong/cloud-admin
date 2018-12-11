package cn.phenix.cloud.admin.app.newMedia.ftsearch.service;

import cn.phenix.cloud.admin.app.newMedia.ftsearch.dao.NewSearchHotMediaMapper;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.model.newFtsearch.NewSearchHotMedia;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mgm
 */
@Service
@Transactional(readOnly = true)
public class NewSearchHotMediaService extends CoreService<NewSearchHotMediaMapper,NewSearchHotMedia>{

    @Transactional
    public void deleteByMediaId(String mediaId) {
        dao.deleteByMediaId(mediaId);
    }


}
