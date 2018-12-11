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
public class NewMediaSeriesTagVo {

    private String mediaSeriesId;
    private String mediaSubjectId;
    private String tagId;

    private String seriesName;
    private String subjectName;
    private Integer location;

    private String originalCountry;
    private String releaseYear;
    private Double score;
    private Integer charge;

    public double getShowCharge() {
        if (this.getCharge() == null) return 0;
        return new BigDecimal(this.getCharge().doubleValue() / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
