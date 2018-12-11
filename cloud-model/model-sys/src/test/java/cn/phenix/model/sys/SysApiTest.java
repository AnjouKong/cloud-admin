package cn.phenix.model.sys;

import cn.phenix.cloud.core.utils.MyBeanUtils;

import java.util.Date;

/**
 * @author xiaobin
 * @create 2017-10-14 上午11:24
 **/
public class SysApiTest {


    public static void main(String[] args) {
        SysApi api = new SysApi();
        api.setId("1");
        api.setCreateBy("1");
        api.setCreateDate(new Date());
        api.setAppId("12");
        api.setAppName("22");
        api.setAppSecret("secret");


        SysApi bean = new SysApi();
        MyBeanUtils.to(api, bean);
        System.out.println("id:" + bean.getId());
        System.out.println("name:" + bean.getAppName());
        System.out.println("createDate:" + bean.getCreateDate());
    }
}
