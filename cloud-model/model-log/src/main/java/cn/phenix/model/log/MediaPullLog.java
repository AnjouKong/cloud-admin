package cn.phenix.model.log;

import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.cloud.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 资源抓取日志描述
 * xx
 * Created by xiaobin on 2017/8/16.
 */
@Getter
@Setter
@Entity
@Table(name="LOG_MEDIA_PULL")
public class MediaPullLog extends BaseModel {

    private static final long serialVersionUID = 4679599898274074307L;

    public MediaPullLog(){}

    //来源
    @Column
    private String source;

    //执行类
    @Column
    private String className;

    //每批次条数
    @Column
    private Integer batchSize;

    //本次转化总条数
    @Column
    private Integer total;

    //执行时长(秒)
    @Column
    private Long whenLong;

    //开始时间
    @Column
    private Date startDate;

    //结束时间
    @Column
    private Date endDate;

    //是否成功(1:成功;2:失败)
    @Column
    private Integer isSuccess;

    @Transient
    public String getStartDateStr(){
        return DateUtils.format(this.getStartDate(),"yyyy-MM-dd HH:mm:ss");
    }

    @Transient
    public String getEndDateStr(){
        return DateUtils.format(this.getEndDate(),"yyyy-MM-dd HH:mm:ss");
    }
}
