package cn.phenix.model.ui.v1.event;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * 图片事件
 * fxq
 */
@Getter
@Setter
public class ImageEvent extends SceneEvent {


    public static final String eventID = "imageEvent";

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
