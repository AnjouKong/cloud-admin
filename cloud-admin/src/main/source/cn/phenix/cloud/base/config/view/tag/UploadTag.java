package cn.phenix.cloud.base.config.view.tag;

import cn.phenix.cloud.admin.mechanism.service.AttMainService;
import cn.phenix.cloud.core.utils.SpringContextHolder;
import cn.phenix.cloud.utils.FreeMarkers;
import cn.phenix.common.utils.Collections3;
import cn.phenix.common.utils.ObjectUtils;
import cn.phenix.model.mechanism.AttMain;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.ByteWriter;
import org.beetl.core.GeneralVarTagBinding;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-10-18 下午7:29
 **/
public class UploadTag extends GeneralVarTagBinding {

    @Override
    public void render() {
        init();

        ByteWriter out = ctx.byteWriter;
        String topAttMainId = "";
        String btnWidth = "80px";
        List<AttMain> attMains = Lists.newArrayList();
        if (StringUtils.isNotBlank(modelId) && StringUtils.isNotBlank(modelName)) {
            AttMainService attMainService = SpringContextHolder.getBean("attMainService");
            attMains = attMainService.getAttMain(modelId, modelName, fdKey);
            if (!Collections3.isEmpty(attMains)) {
                topAttMainId = attMains.get(0).getId();
            }
        }
        if (StringUtils.isBlank(btnText)) {
            btnText = "添加";
        }
        if (btnText.length() > 3) {
            btnWidth = "120px";
        }

        String ext = "";
        if (StringUtils.isNotBlank(exts)) {
            String[] extArray = exts.split(",");
            StringBuilder sb = new StringBuilder();
            for (String string : extArray) {
                sb.append("\"").append(string).append("\",");
            }
            ext = sb.toString();
            ext = ext.substring(0, ext.length() - 1);
        }

        StringBuffer style = new StringBuffer();

        if (StringUtils.isNotBlank(imgWidth)) {
            imgWidth = "width:" + imgWidth + "px;";
        }

        if (StringUtils.isNotBlank(imgHeight)) {
            imgHeight = "height:" + imgHeight + "px";
        }

        if (StringUtils.isNotBlank(imgWidth) || StringUtils.isNotBlank(imgHeight)) {
            style.append("style='").append(imgWidth).append(imgHeight).append("'");
        }
        try {
            if (!"view".equals(method)) {
                out.writeObject("<div class=\"btn-toolbar\" role=\"toolbar\">\n" +
                        "    <div class=\"btn-group\">\n" +
                        "        <button type=\"button\" style=\"width: " + btnWidth + ";\" class=\"" + btnClass + "\" id=\"i_select_files_" + id + "\"> " + btnText + "\n" +
                        "        </button>\n" +
                        "        " + (tipInfo == null ? "" : tipInfo) + "\n" +
                        "        <input id=\"hidden_" + id + "\" data-rule=\"" + dataRule + "\" class=\"" + cssClass + "\" value=\"" + topAttMainId + "\" type=\"hidden\"/>\n" +
                        "    </div>\n" +
                        "</div>");
            }

            out.writeObject("<table class=\"tablesorter\">\n" +
                    "    <tbody id=\"bootstrap-stream-container_" + id + "\">");

            for (AttMain attMain : attMains) {
                out.writeObject("<tr id=\"tr_" + attMain.getId() + "\" class=\"template-upload fade in\">");
                out.writeObject("<td><p class=\"name\">");
                if ("true".equals(showImg)) {
                    if (StringUtils.isNotBlank(attMain.getFdFilePath()) && attMain.getFdFilePath().startsWith("http")) {
                        out.writeObject("<img  src='" + attMain.getFdFilePath() + "'/>");
                    } else {
                        out.writeObject("<img  src='/mechanism/file/image/" + attMain.getId() + "'/>");
                    }
                } else {
                    out.writeObject(attMain.getFdFileName());
                }
                out.writeObject("</p>\n" +
                        "        </td>\n" +
                        "        <td>");
                if (!"view".equals(method)) {
                    out.writeObject("<span style=\"cursor: pointer;\" class=\"glyphicon glyphicon-remove\" onclick=\"javascript:deleteAttMain"+id+"('" + attMain.getId() + "')\">移除</span>|");
                }
                if (StringUtils.isNotBlank(attMain.getFdFilePath()) && attMain.getFdFilePath().startsWith("http")) {
                    out.writeObject("<a style=\"cursor: pointer;\" class=\"glyphicon glyphicon-remove\" target=\"_blank\" href=\"" + attMain.getFdFilePath() + "\">查看</a>");
                } else {
                    out.writeObject("<a style=\"cursor: pointer;\" class=\"glyphicon glyphicon-remove\" target=\"_blank\" href=\"/mechanism/file/view/" + attMain.getId() + "\">查看</a>");
                }
                out.writeObject("</td>\n" +
                        "    </tr>");

            }
            out.writeObject(" </tbody>\n" +
                    "</table>");

            out.writeObject("<div id=\"inputHidden" + id + "\" style=\"display: none\">\n" +
                    "\n" +
                    "</div>");
            Map map = (Map) this.args[1];
            map.put("imgStyle", style.toString());
            map.put("attMains", attMains);
            map.put("ext", ext);
            if (!"view".equals(method)) {
                map.put("method", "edit");
            }
            out.writeObject(FreeMarkers.render("upload", map));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //上传标签id
    private String id;
    //上传标签的name,必须为attMain1、attMain2、attMain3、、。。
    private String name;

    private String modelId;

    private String fdKey;

    //回调函数
    private String callBack;

    private String modelName;

    //是否是多附件
    private String multi = "false";

    private String cssClass;

    //编辑：edit；查看:view
    private String method;

    private String btnClass;

    //上传按钮文本
    private String btnText;

    //是否显示图片
    private String showImg = "false";

    //图片显示宽度
    private String imgWidth;

    //图片显示高度
    private String imgHeight;

    //数据校验
    private String dataRule;

    //允许上传文件的格式,格式：.txt,.rpm
    private String exts;

    private String tipInfo;

    //是否显示上传进度
    private String showUploadInfo = "false";

    private void init() {
        id = ObjectUtils.toString(getAttributeValue("id"));
        name = ObjectUtils.toString(getAttributeValue("name"));
        modelId = ObjectUtils.toString(getAttributeValue("modelId"));
        modelName = ObjectUtils.toString(getAttributeValue("modelName"));
        fdKey = ObjectUtils.toString(getAttributeValue("fdKey"));
        callBack = ObjectUtils.toString(getAttributeValue("callBack"));
        multi = ObjectUtils.toString(getAttributeValue("multi"), "false");
        cssClass = ObjectUtils.toString(getAttributeValue("cssClass"));
        method = ObjectUtils.toString(getAttributeValue("method"));
        btnClass = ObjectUtils.toString(getAttributeValue("btnClass"));
        btnText = ObjectUtils.toString(getAttributeValue("btnText"));
        showImg = ObjectUtils.toString(getAttributeValue("showImg"), "false");
        imgWidth = ObjectUtils.toString(getAttributeValue("imgWidth"));
        imgHeight = ObjectUtils.toString(getAttributeValue("imgHeight"));
        exts = ObjectUtils.toString(getAttributeValue("exts"));
        dataRule = ObjectUtils.toString(getAttributeValue("dataRule"));
        tipInfo = ObjectUtils.toString(getAttributeValue("tipInfo"));
        showUploadInfo = ObjectUtils.toString(getAttributeValue("showUploadInfo"), "false");
    }


}
