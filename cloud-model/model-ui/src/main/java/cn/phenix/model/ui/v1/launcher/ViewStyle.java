package cn.phenix.model.ui.v1.launcher;

import cn.phenix.model.ui.v1.utils.HashCodeUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

/**
 * 容器樣式
 *
 * @author xiaobin
 * @create 2017-10-11 下午2:49
 **/
@Getter
@Setter
@Embeddable
public class ViewStyle {

    /**
     * 文字大小
     */
    private String fontSize;

    /**
     * 字体样式
     */
    private String fontFamily;

    /**
     * 字体背景颜色
     */
    private String fontBackGroupColor;

    /**
     * 文字颜色
     */
    private String fontColor;
    /**
     * 图标
     */
    private String icon;
    /**
     * 图标高度
     */
    private String iconHeight;
    /**
     * 图标宽度
     */
    private String iconWidth;
    /**
     * 图标间距
     */
    private String iconSpacing;

    /**
     * 图标位置 top，bottom，left，right
     */
    private String iconPosition;

    /**
     * 背景颜色
     */
    private String backGroupColor;

    /**
     * 背景图片
     */
    private String backGroupImage;

    /**
     * 宽度
     */
    private String width;

    /**
     * 高度
     */
    private String height;

    /**
     * 相对父窗口位置
     */
    private String parentLeft;

    /**
     * 相对父窗口位置
     */
    private String parentTop;

    @Override
    public int hashCode() {
        return HashCodeUtils.getHashCode(this);
    }
}
