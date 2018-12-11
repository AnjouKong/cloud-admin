package cn.phenix.model.ui.v1;

import cn.phenix.model.ui.v1.enums.SceneAction;
import cn.phenix.model.ui.v1.launcher.Container;
import lombok.Getter;
import lombok.Setter;

/**
 * 场景栏目
 *
 * @author xiaobin
 * @create 2017-10-11 下午3:06
 **/
@Getter
@Setter
public class ColumnUI extends Container {

    public static final String uiClassID = "ColumnUI";

    /**
     * 栏目动作 （未使用）
     */
    private SceneAction action;

    /**
     * 场景窗口 （未使用）
     */
    private WindowUI window;

    /**
     *  允许获得焦点  0否  1是
     */
    private String getFocus;

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (action != null) {
            hashCode += action.hashCode();
        }
        return hashCode;
    }
}
