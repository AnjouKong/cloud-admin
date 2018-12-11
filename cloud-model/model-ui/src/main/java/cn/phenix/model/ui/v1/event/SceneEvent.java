package cn.phenix.model.ui.v1.event;

import cn.phenix.cloud.core.utils.Reflections;
import cn.phenix.model.ui.v1.deserializer.EventDeserializer;
import cn.phenix.model.ui.v1.utils.HashCodeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 场景事件
 * fxq
 */
@Getter
@Setter
@MappedSuperclass
public abstract class SceneEvent implements Serializable {

    /**
     * 事件类型  onclick/onfocus
     */
    private String eventType;

    /**
     * 场景动作
     */
    private String sceneAction;

    /**
     * 事件触发前(未使用)
     */
    @JsonDeserialize(using = EventDeserializer.class)
    private SceneEvent beforeEvent;

    /**
     * 事件完成后(未使用)
     */
    @JsonDeserialize(using = EventDeserializer.class)
    private SceneEvent afterEvent;


    private String eventID;

    @JsonProperty(value = "eventID")
    protected abstract String getEventID();

    @Transient
    @JsonIgnore
    public String getEvent() {
        return (String) Reflections.getFieldValue(this, "eventID");
    }


    public static final String onclick = "onclick";

    public static final String onfocus = "onfocus";


    @Override
    public int hashCode() {
        int hashCode = HashCodeUtils.getHashCode(this);
        if (StringUtils.isNotBlank(eventType)) {
            hashCode += eventType.hashCode();
        }
        if (StringUtils.isNotBlank(sceneAction)) {
            hashCode += sceneAction.hashCode();
        }

        if (beforeEvent != null) {
            hashCode += beforeEvent.hashCode();
        }
        if (beforeEvent != null) {
            hashCode += beforeEvent.hashCode();
        }
        if (afterEvent != null) {
            hashCode += afterEvent.hashCode();
        }

        return hashCode;
    }

}
