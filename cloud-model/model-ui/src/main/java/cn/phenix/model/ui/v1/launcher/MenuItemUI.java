package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;

/**
 * 导航菜单控件
 * @author xiaobin
 * @create 2017-10-16 下午4:49
 **/
@Getter
@Setter
public class MenuItemUI extends Container{

    public static final String uiClassID = "MenuItemUI";

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
