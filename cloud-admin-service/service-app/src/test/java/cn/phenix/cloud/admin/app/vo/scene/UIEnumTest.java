//package cn.phenix.cloud.admin.app.vo.scene;
//
//import cn.phenix.cloud.core.utils.JsonUtils;
//import cn.phenix.model.app.scene.launcher.HtmlUI;
//import cn.phenix.model.app.scene.launcher.PanelUI;
//import cn.phenix.model.app.scene.launcher.ViewStyle;
//import com.google.common.collect.Lists;
//
//import java.util.List;
//
///**
// * @author xiaobin
// * @create 2017-11-28 上午11:30
// **/
//public class UIEnumTest {
//
//    public static void main(String[] args){
//
//        ViewStyle style = new ViewStyle();
//        style.setFontColor("red");
//
//        List<UIEnum> uiEnumList = Lists.newArrayList();
//
//        PanelUI panelUI = new PanelUI();
//        panelUI.setName("panel");
//        panelUI.setStyle(style);
//        //UIEnum.PanelUI.setUi(panelUI);
//        UIEnum panelEnum = UIEnum.PanelUI;
//        panelEnum.setUi(panelUI);
//        uiEnumList.add(panelEnum);
//
//
//        HtmlUI htmlUI = new HtmlUI();
//        htmlUI.setName("html");
//        htmlUI.setStyle(style);
//
//        UIEnum htmlEnum = UIEnum.HtmlUI;
//        htmlEnum.setUi(htmlUI);
//        uiEnumList.add(htmlEnum);
//
//        AppUITest uiTest = new AppUITest();
//        uiTest.setName("appUi");
//        uiTest.setUiEnumList(uiEnumList);
//
//        System.out.println(JsonUtils.writeObjectToJson(uiTest));
//
//    }
//}
