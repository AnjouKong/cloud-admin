package cn.phenix.cloud.admin.app.media.statistics.service;

import cn.phenix.cloud.admin.app.media.tag.dao.MediaTagMapper;
import cn.phenix.cloud.admin.app.vo.media.MediaSeriesTagVo;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.media.MediaSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 马刚铭 .
 */
@Service
@Transactional(readOnly = true)
public class StatisticsService {
    @Autowired
    private MediaTagMapper mediaTagMapper;

    public List<MediaSeriesTagVo> search(MediaSeries mediaSeries, String tagId) {

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
            BigDecimal bd= mediaSeries.getCharge().setScale(2, BigDecimal.ROUND_HALF_UP);
            parameter.insert("charge", bd);
        }
        if (!Strings.isBlank(mediaSeries.getLevelId())) {
            parameter.insert("levelId", mediaSeries.getLevelId());
        }

        List<MediaSeriesTagVo> seriesVo = mediaTagMapper.findByNamedQuery(mediaTagMapper.seriesVoSearch, parameter, MediaSeriesTagVo.class);

        return seriesVo;
    }


}
