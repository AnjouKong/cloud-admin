package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;

/**
 * 文本
 *
 * @author fxq
 * @create 2018-3-12
 **/
@Getter
@Setter
public class TextUI extends Container {

    public static final String uiClassID = "TextUI";

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
