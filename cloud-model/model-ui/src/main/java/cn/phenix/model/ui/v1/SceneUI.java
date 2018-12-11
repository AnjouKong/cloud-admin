package cn.phenix.model.ui.v1;

import cn.phenix.model.ui.v1.launcher.PanelUI;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 场景
 *
 * @author xiaobin
 * @create 2017-10-11 下午4:47
 **/
@Getter
@Setter
public class SceneUI extends PanelUI {

    public static final String uiClassID = "SceneUI";

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 导航
     */
    private NavigationUI navigation;

    /**
     * 状态栏
     */
    private StatusBarUI statusBar;

    /**
     * 渲染前回调事件
     */
    private String beforeAction;

    /**
     * 渲染后回调事件
     */
    private String afterAction;

    /**
     * 闲置时间
     */
    private Integer unusedTime;

    @Override
    public String getUiClass() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (StringUtils.isNotBlank(sceneName)) {
            hashCode += sceneName.hashCode();
        }
        if (navigation != null) {
            hashCode += navigation.hashCode();
        }
        if (statusBar != null) {
            hashCode += statusBar.hashCode();
        }
        if (beforeAction != null) {
            hashCode += beforeAction.hashCode();
        }
        if (afterAction != null) {
            hashCode += afterAction.hashCode();
        }
        if (unusedTime != null) {
            hashCode += unusedTime;
        }
        return hashCode;
    }
}
