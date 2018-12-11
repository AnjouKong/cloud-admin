package cn.phenix.model.ui.v1.event;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * 广告事件
 * fxq
 */
@Getter
@Setter
public class AdvEvent extends SceneEvent {


    public static final String eventID = "advEvent";

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
