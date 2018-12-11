package cn.phenix.model.ui.v1;

import cn.phenix.model.ui.v1.launcher.Container;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 挂件
 *
 * @author xiaobin
 * @create 2017-10-11 下午5:07
 **/
@Getter
@Setter
public class PendantUI extends Container {

    /**
     * 挂件类型
     */
    private String pendantType;

    public static final String uiClassID = "PendantUI";

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    /**
     * 时间挂件
     */
    public static final String time = "time";
    /**
     * 天气挂件
     */
    public static final String weather = "weather";
    /**
     * 地区挂件
     */
    public static final String area = "area";
    /**
     * 语言挂件
     */
    public static final String language = "language";
    /**
     * 日期挂件
     */
    public static final String date = "date";

    /**
     * wifi挂件
     */
    public static final String wifi = "wifi";

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (StringUtils.isNotBlank(pendantType)) {
            hashCode += pendantType.hashCode();
        }
        return hashCode;
    }

}
