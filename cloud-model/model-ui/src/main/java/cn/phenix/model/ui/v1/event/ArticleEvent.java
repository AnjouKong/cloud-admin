package cn.phenix.model.ui.v1.event;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * 文章事件
 * fxq
 */
@Getter
@Setter
public class ArticleEvent extends SceneEvent {


    public static final String eventID = "articleEvent";


    private String rule;

    @Override
    protected String getEventID() {
        return eventID;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (StringUtils.isNotBlank(rule)) {
            hashCode += rule.hashCode();
        }
        return hashCode;
    }

}
