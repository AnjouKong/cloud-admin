package cn.phenix.cloud.admin.app.media.ftsearch.service;

import cn.phenix.cloud.admin.app.media.ftsearch.dao.FtsearchHotwordMediaMapper;
import cn.phenix.cloud.admin.app.media.ftsearch.dao.FtsearchRecommendMediaMapper;
import cn.phenix.cloud.admin.app.media.media.dao.MediaMapper;
import cn.phenix.cloud.admin.app.media.media.dao.MediaSeriesFuncMapper;
import cn.phenix.cloud.admin.app.media.media.dao.MediaSeriesMapper;
import cn.phenix.cloud.admin.app.media.tag.dao.MediaTagMapper;
import cn.phenix.cloud.admin.app.vo.media.MediaSeriesTagVo;
import cn.phenix.cloud.admin.sys.service.SysDictService;
import cn.phenix.cloud.core.utils.MyBeanUtils;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.cloud.jpa.dynamic.Parameter;
import cn.phenix.model.app.media.MediaSeries;
import cn.phenix.model.app.media.MediaSeriesFunc;
import cn.phenix.model.app.media.MediaTag;
import cn.phenix.model.ftsearch.FtsearchHotwordMedia;
import cn.phenix.model.sys.SysDict;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static cn.phenix.cloud.admin.app.media.tag.dao.MediaTagMapper.tagNowList;

/**
 * Created by mgm
 */
@Service
@Transactional(readOnly = true)
public class FtsearchHotwordMediaService extends CoreService<FtsearchHotwordMediaMapper,FtsearchHotwordMedia>{

    @Transactional
    public void deleteByLevelIdAndMediaId(String levelId, String mediaId) {
        dao.deleteByLevelIdAndMediaId(levelId,mediaId);
    }


}
