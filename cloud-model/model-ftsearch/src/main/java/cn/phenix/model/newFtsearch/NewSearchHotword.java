package cn.phenix.model.newFtsearch;

import cn.phenix.cloud.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 热词搜索
 *
 * @author xiaobin
 * @create 2018-03-12 上午10:46
 **/
@Setter
@Getter
@Table(name = "search_hotword")
@Entity
@DynamicInsert
@DynamicUpdate
public class NewSearchHotword extends BaseModel implements Serializable {

    /**
     * 热词
     */
    protected String hotWord;
    /**
     * 搜索次数
     */
    protected Integer searchFrequency;
    /**
     * 是否屏蔽
     */
    protected Boolean shieldFlag;
    /**
     * 排序
     */
    protected Integer hotOrder;
}
