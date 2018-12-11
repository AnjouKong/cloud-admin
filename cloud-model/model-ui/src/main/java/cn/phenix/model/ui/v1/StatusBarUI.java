package cn.phenix.model.ui.v1;

import cn.phenix.model.ui.v1.launcher.PanelUI;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 状态栏目(头部)
 *
 * @author xiaobin
 * @create 2017-10-11 下午5:06
 **/
@Getter
@Setter
public class StatusBarUI extends PanelUI {

    public static final String uiClassID = "StatusBarUI";
    /**
     * 挂件
     */
    private List<PendantUI> pendantList = Lists.newArrayList();


    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        if (CollectionUtils.isNotEmpty(pendantList)) {
            for (PendantUI pendantUI : pendantList) {
                hashCode += pendantUI.hashCode();
            }
        }
        return hashCode;
    }

}
