package cn.phenix.model.ui.v1.launcher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 面板控件
 *
 * @author xiaobin
 * @create 2017-10-16 下午5:24
 **/
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PanelUI extends Container {

    public static final String uiClassID = "PanelUI";

    private Integer itemSpacing;    //无用，后续废除

    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
