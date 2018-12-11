package cn.phenix.model.sys;

import cn.phenix.cloud.core.model.TreeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 区域Entity
 */
@Getter
@Setter
@Entity
@Table(name = "sys_area")
public class SysArea extends TreeEntity<SysArea> {

    private static final long serialVersionUID = 1L;
    private String code;    // 区域编码
    private String type;    // 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
    private String areaCode;    // 城市编码

    public SysArea() {
        super();
        this.sort = 30;
    }


    public SysArea getParent() {
        return parent;
    }

    public void setParent(SysArea parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }
}