package cn.phenix.model.ui.v1.event;

import cn.phenix.model.ui.v1.deserializer.UIDeserializer;
import cn.phenix.model.ui.v1.launcher.Container;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

/**
 * 容器事件
 * fxq
 */
@Getter
@Setter
public class ContainerEvent extends SceneEvent {


    public static final String eventID = "ContainerEvent";

    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
    @JsonDeserialize(using = UIDeserializer.class)
    private Container rule;

    @Override
    protected String getEventID() {
        return eventID;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (rule != null) {
            hashCode += rule.hashCode();
        }
        return hashCode;
    }
}
