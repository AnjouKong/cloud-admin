package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;

/**
 * 标签
 *
 * @author xiaobin
 * @create 2017-10-14 下午1:48
 **/
@Getter
@Setter
public class LabelUI extends Container{

    public static final String uiClassID = "LabelUI";

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
