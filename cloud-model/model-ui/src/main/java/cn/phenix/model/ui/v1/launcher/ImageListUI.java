package cn.phenix.model.ui.v1.launcher;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 图片集合
 *
 * @author xiaobin
 * @create 2017-10-16 下午4:26
 **/
@Getter
@Setter
public class ImageListUI extends Container {

    public static final String uiClassID = "ImageListUI";

    private List<ImageUI> imageUIList = Lists.newArrayList();

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (CollectionUtils.isNotEmpty(imageUIList)) {
            for (ImageUI imageUI : imageUIList) {
                hashCode += imageUI.hashCode();
            }
        }
        return hashCode;
    }
}
