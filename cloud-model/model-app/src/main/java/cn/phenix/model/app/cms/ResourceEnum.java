package cn.phenix.model.app.cms;

/**
 * 资源类型
 *
 * @author xiaobin
 * @create 2018-01-10 下午6:04
 **/
public enum ResourceEnum {

    ad("广告"),
    img("图片"),
    imgCollection("图片集合"),
    app("应用"),
    html("html"),
    video("视频"),
    pms("PMS"),
    music("音乐"),
    language("语言"),
    website("网站"),
    theme("主题包"),
    custom("自定义"),
    secondary("二级页面");

    private String text;

    ResourceEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
