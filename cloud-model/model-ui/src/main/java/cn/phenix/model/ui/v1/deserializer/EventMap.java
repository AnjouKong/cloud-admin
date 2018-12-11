package cn.phenix.model.ui.v1.deserializer;

import cn.phenix.model.ui.v1.event.*;
import com.google.common.collect.Maps;

import java.util.Map;

 abstract class EventMap {

    static Map<String, Class<? extends SceneEvent>> VALUES = Maps.newConcurrentMap();

    static {
        VALUES.put("advEvent", AdvEvent.class);
        VALUES.put("SpecialTVSignalEvent", SpecialTVSignalEvent.class);
        VALUES.put("GeneralTVSignalEvent", GeneralTVSignalEvent.class);
        VALUES.put("appEvent", AppEvent.class);
        VALUES.put("articleEvent", ArticleEvent.class);
        VALUES.put("ContainerEvent", ContainerEvent.class);
        VALUES.put("HtmlEvent", HtmlEvent.class);
        VALUES.put("imageEvent", ImageEvent.class);
        VALUES.put("imageListEvent", ImageListEvent.class);
        VALUES.put("videoEvent", VideoEvent.class);
        VALUES.put("CustomEvent", CustomEvent.class);
        VALUES.put("RecommendedEvent", RecommendedEvent.class);
        VALUES.put("MenuEvent", MenuEvent.class);
        VALUES.put("ShowEvent", ShowEvent.class);
        VALUES.put("websiteEvent", WebsiteEvent.class);
        VALUES.put("secondaryEvent", SecondaryEvent.class);
    }
}
