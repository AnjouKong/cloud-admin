package cn.phenix.model.ui.v1;

import cn.phenix.model.ui.v1.launcher.PanelUI;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 场景窗口
 *
 * @author xiaobin
 * @create 2017-10-11 下午3:01
 **/
@Getter
@Setter
public class WindowUI extends PanelUI {

    public static final String uiClassID = "PanelUI";

    /**
     * 推荐位
     */
    private List<RecommendedUI> recommendedList = Lists.newArrayList();

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (!CollectionUtils.isEmpty(recommendedList)) {
            for (RecommendedUI ui : recommendedList) {
                hashCode += ui.hashCode();
            }
        }
        return hashCode;
    }

}
