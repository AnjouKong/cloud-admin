package cn.phenix.model.ui.v1.deserializer;

import cn.phenix.model.ui.v1.ColumnUI;
import cn.phenix.model.ui.v1.PendantUI;
import cn.phenix.model.ui.v1.RecommendedUI;
import cn.phenix.model.ui.v1.launcher.*;
import com.google.common.collect.Maps;

import java.util.Map;

 abstract class UIMap {
    static Map<String, Class<? extends Container>> VALUES = Maps.newConcurrentMap();

    static {
        VALUES.put("AppUI", AppUI.class);
        VALUES.put("PanelUI", PanelUI.class);
        VALUES.put("HtmlUI", HtmlUI.class);
        VALUES.put("ColumnUI", ColumnUI.class);
        VALUES.put("PendantUI", PendantUI.class);
        VALUES.put("ImageListUI", ImageListUI.class);
        VALUES.put("ImageUI", ImageUI.class);
        VALUES.put("JavaCodeUI", JavaCodeUI.class);
        VALUES.put("LabelUI", LabelUI.class);
        VALUES.put("MenuItemUI", MenuItemUI.class);
        VALUES.put("MenuUI", MenuUI.class);
        VALUES.put("VideoUI", VideoUI.class);
        VALUES.put("WebUI", WebUI.class);
        VALUES.put("RecommendedUI", RecommendedUI.class);
        VALUES.put("PmsUI", PmsUI.class);
        VALUES.put("ShowUI", ShowUI.class);
        VALUES.put("AnnouncementUI", AnnouncementUI.class);
        VALUES.put("WifiUI", WifiUI.class);
    }
}
