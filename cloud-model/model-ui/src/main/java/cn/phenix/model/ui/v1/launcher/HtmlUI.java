package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;

/**
 * 段落文本控件
 *
 * @author xiaobin
 * @create 2017-10-16 下午4:35
 **/
@Getter
@Setter
public class HtmlUI extends Container {

    public static final String uiClassID = "HtmlUI";

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
