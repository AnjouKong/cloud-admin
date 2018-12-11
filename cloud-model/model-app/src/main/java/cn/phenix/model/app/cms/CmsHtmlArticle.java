package cn.phenix.model.app.cms;


import cn.phenix.cloud.core.model.BaseModel;
import cn.phenix.cloud.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "cms_html_article")
public class CmsHtmlArticle extends BaseModel {

    @Column
    private String htmlId;

    @Column
    private String title;

    @Column
    private String info;

    @Column
    private String content;

    @Column
    private String languageCode;


    public static final String modelName = "cn.phenix.model.app.cms.CmsHtmlArticle";

    @Transient
    public String getUpdateDateStr(){
        return DateUtils.format(this.getUpdateDate(),"yyyy-MM-dd HH:mm:ss");
    }

}
