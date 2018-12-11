package cn.phenix.cloud.admin.app.vo.scene;

import cn.phenix.model.ui.v1.launcher.Container;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author xiaobin
 * @create 2017-11-28 上午11:21
 **/
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UIEnum {

    PanelUI {
        cn.phenix.model.ui.v1.launcher.PanelUI ui;

        public cn.phenix.model.ui.v1.launcher.PanelUI getUI() {
            return ui;
        }

        @Override
        public <T extends Container> void setUi(T ui) {
            this.ui = (cn.phenix.model.ui.v1.launcher.PanelUI) ui;
        }
    },
    HtmlUI {
        cn.phenix.model.ui.v1.launcher.HtmlUI ui;

        public cn.phenix.model.ui.v1.launcher.HtmlUI getUI() {
            return ui;
        }

        @Override
        public <T extends Container> void setUi(T ui) {
            this.ui = (cn.phenix.model.ui.v1.launcher.HtmlUI) ui;
        }
    };

    public abstract Container getUI();

    public abstract <T extends Container> void setUi(T ui);

}
