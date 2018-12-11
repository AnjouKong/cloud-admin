package cn.phenix.model.ui.v1.launcher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * App
 *
 * @author xiaobin
 * @create 2017-10-14 下午1:48
 **/
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppUI extends Container {

    public static final String uiClassID = "AppUI";

    /**
     * 布局  vertical 垂直，horizontal 水平（未使用）
     */
    private String layout;

    /**
     * 版面风格  扁平化布局 delayering  弹出菜单 popupMenu  卡片 card  极简UI minimalist
     */
    private String layoutStyle;

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
