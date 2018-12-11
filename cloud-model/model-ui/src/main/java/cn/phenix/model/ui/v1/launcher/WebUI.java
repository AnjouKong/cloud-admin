package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * web控件
 *
 * @author xiaobin
 * @create 2017-10-16 下午4:28
 **/
@Getter
@Setter
public class WebUI extends Container {

    public static final String uiClassID = "WebUI";

    private String url;

    //渲染方式（LOCAL:下载后，本地渲染；HTTP:HTTP网络渲染）
    private String render;

    /**
     * 下载后，本地渲染
     */
    public static final String RENDER_LOCAL = "local";

    /**
     * HTTP网络渲染
     */
    public static final String RENDER_HTTP = "HTTP";

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

        if (StringUtils.isNotBlank(render)) {
            hashCode += render.hashCode();
        }
        return hashCode;
    }
}
