package cn.phenix.cloud.admin.app.vo.media;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 媒资标签VO
 * Created by mgm
 */
@Getter
@Setter
public class MediaSeriesTagVo {

    private String mediaSeriesId;
    private String mediaSubjectId;
    private String tagId;

    private String seriesName;
    private String subjectName;
    private Integer location;

    private String originalCountry;
    private String releaseYear;
    private BigDecimal score;
    private BigDecimal charge;
}
