package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;

/**
 * 导航控件
 *
 * @author xiaobin
 * @create 2017-10-16 下午4:53
 **/
@Getter
@Setter
public class MenuUI extends Container {

    public static final String uiClassID = "MenuUI";

    /**
     * 条目间距
     */
    private Integer itemSpacing;

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
