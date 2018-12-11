package cn.phenix.model.ui.v1;


import cn.phenix.model.ui.v1.enums.SceneAction;
import cn.phenix.model.ui.v1.launcher.PanelUI;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 场景导航
 *
 * @author xiaobin
 * @create 2017-10-11 下午1:00
 **/
@Getter
@Setter
public class NavigationUI extends PanelUI {

    public static final String uiClassID = "NavigationUI";

    /**
     * 导航动作
     */
    private SceneAction action;

    /**
     * 场景栏目
     */
    private List<ColumnUI> columnList = Lists.newArrayList();

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (action != null) {
            hashCode += action.hashCode();
        }
        if (CollectionUtils.isNotEmpty(columnList)) {
            for (ColumnUI ui : columnList) {
                hashCode += ui.hashCode();
            }
        }
        return hashCode;
    }
}
