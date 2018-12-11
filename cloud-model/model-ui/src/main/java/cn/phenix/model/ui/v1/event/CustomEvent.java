package cn.phenix.model.ui.v1.event;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * 自定义事件
 * fxq
 */
@Getter
@Setter
public class CustomEvent extends SceneEvent {


    public static final String eventID = "CustomEvent";

    /**
     * 格式示例： {"packageName":"com.unitedview.phenix.vod","action":"com.unitedview.phenix.vod.vedio.detail","param":"12589409"}
     */
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
