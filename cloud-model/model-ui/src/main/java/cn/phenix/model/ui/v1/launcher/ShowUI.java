package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;

/**
 * 展示控件
 *
 * @author xiaobin
 * @create 2017-10-16 下午5:24
 **/
@Getter
@Setter
public class ShowUI extends Container {

    public static final String uiClassID = "ShowUI";

    private Integer itemSpacing;    //无用，后续废除

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
