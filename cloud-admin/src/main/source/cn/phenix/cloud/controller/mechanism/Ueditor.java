package cn.phenix.cloud.controller.mechanism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaobin268 on 2014-11-6.
 */
public class Ueditor {

    public static final Map<String, Object> map;

    public static List<String> imageList = new ArrayList<String>();

    public static List<String> fileList = new ArrayList<String>();

    public static List<String> videoList = new ArrayList<String>();


    static {
        imageList.add(".png");
        imageList.add(".jpg");
        imageList.add(".jpeg");
        imageList.add(".gif");
        imageList.add(".bmp");

        map = new HashMap<>();
        map.put("imageActionName", "uploadimage");

        map.put("imageFieldName", "upfile");
        map.put("imageMaxSize", "20480000000");

        map.put("imageAllowFiles", imageList);
        map.put("imageCompressEnable", true);
        map.put("imageCompressBorder", 1600);


        map.put("fileActionName", "uploadfile");

        map.put("fileFieldName", "upfile");
        map.put("fileMaxSize", "5120000000");

        fileList.addAll(imageList);
        fileList.add(".rar");
        fileList.add(".zip");
        fileList.add(".tar");
        fileList.add(".doc");
        fileList.add(".docx");
        fileList.add(".xls");
        fileList.add(".xlsx");
        fileList.add(".ppt");
        fileList.add(".pptx");
        fileList.add("");

        map.put("fileAllowFiles", fileList);


        map.put("videoActionName","uploadvideo");

        videoList.add(".mp4");

        map.put("videoAllowFiles", videoList);
        map.put("videoMaxSize", "102400000");
        map.put("videoFieldName", "upfile");

    }
}
