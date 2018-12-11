package cn.phenix.cloud.base.web.view;

import org.beetl.core.Format;

/**
 * @author xiaobin
 * @create 2017-09-25 下午7:19
 **/
public class HtmlEscapeFormat implements Format {

    public Object format(Object data, String pattern) {
        //return Strings.escapeHtml(String.valueOf(data == null ? "" : data));
        return data;
    }

}
