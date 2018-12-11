package cn.phenix.model.mechanism;

import cn.phenix.cloud.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;

/**
 * 附件
 *
 * @author xiaobin
 * @create 2017-10-17 下午5:29
 **/
@Getter
@Setter
@Entity
@Table(name = "mechanism_att_main")
public class AttMain extends BaseModel {

    //文档类型 用于下载
    @Length(min = 0, max = 255, message = "fd_content_type长度必须介于 0 和 255 之间")
    private String fdContentType;        // fd_content_type

    private String fdData;        // fd_data
    //文档名称
    @Length(min = 0, max = 255, message = "fd_file_name长度必须介于 0 和 255 之间")
    private String fdFileName;        // fd_file_name
    //文档保存路径
    @Length(min = 0, max = 255, message = "fd_file_path长度必须介于 0 和 255 之间")
    private String fdFilePath;        // fd_file_path
    //文档格式、后缀
    @Length(min = 0, max = 50, message = "fd_file_type长度必须介于 0 和 50 之间")
    private String fdFileType;        // fd_file_type
    //文档key,表示某一类文件
    @Length(min = 0, max = 100, message = "fd_key长度必须介于 0 和 100 之间")
    private String fdKey;        // fd_key
    //业务ID
    @Length(min = 0, max = 50, message = "fd_model_id长度必须介于 0 和 50 之间")
    private String fdModelId;        // fd_model_id
    //业务名称
    @Length(min = 0, max = 255, message = "fd_model_name长度必须介于 0 和 255 之间")
    private String fdModelName;        // fd_model_name
    //文档排序
    @Length(min = 0, max = 10, message = "fd_order长度必须介于 0 和 10 之间")
    private String fdOrder;        // fd_oder
    //文档大小
    private Double fdSize;        // fd_size
    //文档存储方式，oss,sys
    @Length(min = 0, max = 30, message = "fd_store_type长度必须介于 0 和 30 之间")
    private String fdStoreType;        // fd_store_type
    //文档转换标示0:待转换；1:转换中；2:转换完成；3:转换失败
    private String flag;        // flag
    //文件md5 唯一标示
    private String token;        // token
    //创建者
    private String createById;        // create_by_id

    private Long crc32;        // crc32
    private String reSizePath;        // re_size_path
    private String videoView;        // video_view

}
