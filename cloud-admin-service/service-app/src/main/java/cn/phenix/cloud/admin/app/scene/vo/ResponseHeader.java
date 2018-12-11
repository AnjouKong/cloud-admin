package cn.phenix.cloud.admin.app.scene.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 接口返回json
 **/
@Getter
@Setter
public class ResponseHeader {

    private String bodyType;
    private int code;
    private String msg;

}
