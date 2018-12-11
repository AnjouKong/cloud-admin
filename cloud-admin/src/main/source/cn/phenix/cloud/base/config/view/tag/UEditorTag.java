package cn.phenix.cloud.base.config.view.tag;

import cn.phenix.cloud.utils.FreeMarkers;
import cn.phenix.common.utils.ObjectUtils;
import org.beetl.core.ByteWriter;
import org.beetl.core.GeneralVarTagBinding;

import java.io.IOException;
import java.util.Map;

/**
 * 富文本标签
 *
 * @author xiaobin
 * @create 2018-01-18 上午10:26
 **/
public class UEditorTag extends GeneralVarTagBinding {

    @Override
    public void render() {
        init();
        ByteWriter out = ctx.byteWriter;
        Map map = (Map) this.args[1];
        try {
            out.writeObject(FreeMarkers.render("ueditor", map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //tag id
    private String id;

    private String width;

    private String height;
    //值
    private String value;

    //随机值
    private String random;

    //失去焦点时回掉函数
    private String onBlurBack;
    //获得焦点时回掉函数
    private String onFocusBack;

    private void init() {
        id = ObjectUtils.toString(getAttributeValue("id"));
        width = ObjectUtils.toString(getAttributeValue("width"));
        height = ObjectUtils.toString(getAttributeValue("height"));
        value = ObjectUtils.toString(getAttributeValue("value"));
        random = ObjectUtils.toString(getAttributeValue("random"));
        onBlurBack = ObjectUtils.toString(getAttributeValue("onBlurBack"));
        onFocusBack = ObjectUtils.toString(getAttributeValue("onFocusBack"));
    }
}
