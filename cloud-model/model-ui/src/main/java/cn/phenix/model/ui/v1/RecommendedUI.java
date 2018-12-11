package cn.phenix.model.ui.v1;

import cn.phenix.model.ui.v1.launcher.PanelUI;
import lombok.Getter;
import lombok.Setter;

/**
 * 推荐位
 *
 * @author xiaobin
 * @create 2017-10-11 下午4:46
 **/
@Getter
@Setter
public class RecommendedUI extends PanelUI {

    public static final String uiClassID = "RecommendedUI";

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }


}
