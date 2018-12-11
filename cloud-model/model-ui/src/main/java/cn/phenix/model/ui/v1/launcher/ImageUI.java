package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;

/**
 * 图片渲染
 *
 * @author xiaobin
 * @create 2017-10-14 下午1:50
 **/
@Getter
@Setter
public class ImageUI extends Container {

    public static final String uiClassID = "ImageUI";

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
