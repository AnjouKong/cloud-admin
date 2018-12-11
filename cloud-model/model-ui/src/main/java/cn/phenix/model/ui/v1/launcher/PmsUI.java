package cn.phenix.model.ui.v1.launcher;

import lombok.Getter;
import lombok.Setter;

/**
 * Pms
 *
 * @author fxq
 **/
@Getter
@Setter
public class PmsUI extends Container {

    public static final String uiClassID = "PmsUI";

    /**
     *  是否滚动   0否 1是
     */
    private String roll;


    @Override
    protected String getUiClassID() {
        return uiClassID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
