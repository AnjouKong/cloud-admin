package cn.phenix.model.ui.v1.event;

import lombok.Getter;
import lombok.Setter;

/**
 * 推荐位容器事件
 * fxq
 */
@Getter
@Setter
public class RecommendedEvent extends ContainerEvent {

    public static final String eventID = "RecommendedEvent";

    @Override
    protected String getEventID() {
        return eventID;
    }

}
