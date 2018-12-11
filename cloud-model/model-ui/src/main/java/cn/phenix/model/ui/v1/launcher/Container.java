package cn.phenix.model.ui.v1.launcher;

import cn.phenix.cloud.core.model.BaseIncrementIdModel;
import cn.phenix.cloud.core.utils.Reflections;
import cn.phenix.model.ui.v1.deserializer.EventDeserializers;
import cn.phenix.model.ui.v1.deserializer.UIDeserializers;
import cn.phenix.model.ui.v1.event.SceneEvent;
import cn.phenix.model.ui.v1.utils.HashCodeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 容器
 *
 * @author xiaobin
 * @create 2017-10-11 下午4:40
 **/
@Getter
@Setter
@MappedSuperclass
public abstract class Container extends BaseIncrementIdModel {

    /**
     * 容器名稱
     */
    private Map<String, String> name = Maps.newConcurrentMap();

    /**
     * 别名  (全季日志使用)
     */
    private String alias;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 容器文本 （未使用）
     */
    private String text;
    /**
     * 是否默认打开   0否  1是
     */
    private String defaultOpen;
    /**
     * 容器是否支持遥控器左右换屏   0否  1是
     */
    private String focusChange;
    /**
     * 容器是否向下授权   0否  1是
     */
    private String isAuthorize;
    /**
     * 角标     0 蓝光，1 杜比，2 ‘4K’（未使用）
     */
    private String corner;
    /**
     * 放大倍数
     */
    private float magnification;
    /**
     * 容器无操作样式
     */
    @Embedded
    private ViewStyle style;
    /**
     * 容器聚焦样式
     */
    @Embedded
    private ViewStyle onfocusStyle;
    /**
     * 容器点击样式
     */
    @Embedded
    private ViewStyle onclickStyle;

    private String uiClassID;

    //子容器
    @JsonDeserialize(using = UIDeserializers.class)
    private List<? extends Container> subContainer = new ArrayList<>();

    //容器事件
    @JsonDeserialize(using = EventDeserializers.class)
    private List<? extends SceneEvent> sceneEvents = new ArrayList<>();

    @JsonProperty(value = "uiClassID")
    protected abstract String getUiClassID();

    @Transient
    @JsonIgnore
    public String getUiClass() {
        return (String) Reflections.getFieldValue(this, "uiClassID");
    }

    @Override
    public int hashCode() {
        int hashCode = HashCodeUtils.getHashCode(this, id);
        if (!CollectionUtils.isEmpty(name)) {
            for (String str : name.keySet()) {
                hashCode += name.get(str).hashCode();
            }
        }
        if (corner != null) {
            hashCode += corner.hashCode();
        }
        if (isAuthorize != null) {
            hashCode += isAuthorize.hashCode();
        }
        if (style != null) {
            hashCode += style.hashCode();
        }
        if (onfocusStyle != null) {
            hashCode += onfocusStyle.hashCode();
        }
        if (onclickStyle != null) {
            hashCode += onclickStyle.hashCode();
        }
        if (!CollectionUtils.isEmpty(subContainer)) {
            for (Container container : subContainer) {
                hashCode += container.hashCode();
            }
        }
        if (!CollectionUtils.isEmpty(sceneEvents)) {
            for (SceneEvent container : sceneEvents) {
                hashCode += container.hashCode();
            }
        }
        return hashCode;
    }
}
