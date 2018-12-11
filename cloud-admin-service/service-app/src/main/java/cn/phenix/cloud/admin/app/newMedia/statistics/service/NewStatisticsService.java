package cn.phenix.cloud.admin.app.newMedia.statistics.service;

import cn.phenix.cloud.admin.app.newMedia.tag.dao.NewMediaTagMapper;
import cn.phenix.cloud.admin.app.vo.media.NewMediaSeriesTagVo;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.newMedia.NewMediaSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 马刚铭 .
 */
@Service
@Transactional(readOnly = true)
public class NewStatisticsService {
    @Autowired
    private NewMediaTagMapper mediaTagMapper;

    public List<NewMediaSeriesTagVo> search(NewMediaSeries mediaSeries, String tagId) {

        //查询条件
        Parameter parameter = Parameter.create();
        if (!Strings.isBlank(mediaSeries.getReleaseYear())) {
            parameter.insert("releaseYear", mediaSeries.getReleaseYear());
        }
        if (!Strings.isBlank(tagId)) {
            parameter.insert("tagId", tagId);
        }
        if (mediaSeries.getScore() != null) {
            parameter.insert("score", mediaSeries.getScore());
        }
        if (mediaSeries.getCharge() != null) {
            parameter.insert("charge", mediaSeries.getCharge());
        }

        List<NewMediaSeriesTagVo> seriesVo = mediaTagMapper.findByNamedQuery(mediaTagMapper.seriesVoSearch, parameter, NewMediaSeriesTagVo.class);

        return seriesVo;
    }


}
