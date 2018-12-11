package cn.phenix.model.ui.v1.event;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * html事件
 * fxq
 */
@Getter
@Setter
public class HtmlEvent extends SceneEvent {


    public static final String eventID = "htmlEvent";

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
