package cn.phenix.model.ui.v1.event;

import lombok.Getter;
import lombok.Setter;

/**
 * 内容展示事件
 * fxq
 */
@Getter
@Setter
public class ShowEvent extends ContainerEvent {


    public static final String eventID = "ShowEvent";

    @Override
    protected String getEventID() {
        return eventID;
    }
}
