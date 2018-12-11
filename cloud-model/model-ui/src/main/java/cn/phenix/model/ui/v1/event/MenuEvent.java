package cn.phenix.model.ui.v1.event;

import lombok.Getter;
import lombok.Setter;

/**
 * 推荐位容器事件
 * fxq
 */
@Getter
@Setter
public class MenuEvent extends ContainerEvent {


    public static final String eventID = "MenuEvent";

    @Override
    protected String getEventID() {
        return eventID;
    }
}
