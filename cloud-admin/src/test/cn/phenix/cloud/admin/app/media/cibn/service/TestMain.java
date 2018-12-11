package cn.phenix.cloud.admin.app.media.cibn.service;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaobin
 * @create 2018-01-12 下午6:45
 **/
public class TestMain {

    public static void main(String[] args){
        String fdFilePath = "http://phenix-att-test.oss-cn-shanghai.aliyuncs.com/201801/B2146779831_57144.jpg";
        String str = StringUtils.substringAfterLast(fdFilePath, "/");
        System.out.println(str);
    }
}
