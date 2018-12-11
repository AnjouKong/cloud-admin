package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * 视频控件
 * @author xiaobin
 * @create 2017-10-16 上午9:51
 **/
@Getter
@Setter
public class VideoUI extends Container{

    public static final String uiClassID = "VideoUI";

    private String url;

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (StringUtils.isNotBlank(url)) {
            hashCode += url.hashCode();
        }
        return hashCode;
    }
}
