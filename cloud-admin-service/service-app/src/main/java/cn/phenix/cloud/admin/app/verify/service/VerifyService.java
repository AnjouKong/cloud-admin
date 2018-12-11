package cn.phenix.cloud.admin.app.verify.service;//package cn.phenix.cloud.admin.tenant.verify.service;


import cn.phenix.cloud.admin.app.verify.dao.VerifyMapper;
import cn.phenix.cloud.admin.app.verify.dao.VerifyRecordMapper;
import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.cloud.jpa.CoreService;
import cn.phenix.cloud.jpa.data.DataTableParameter;
import cn.phenix.cloud.jpa.data.Finder;
import cn.phenix.cloud.jpa.data.PageTable;
import cn.phenix.model.app.media.MediaSeries;
import cn.phenix.model.tenant.verify.VerifyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 马刚铭 on 2017/8/16.
 */
@Service
@Transactional(readOnly = true)
public class VerifyService extends CoreService<VerifyMapper, MediaSeries> {
    @Autowired
    private VerifyMapper verifyMapper;
    @Autowired
    private VerifyRecordMapper verifyRecordMapper;

    public PageTable<MediaSeries> findPage(DataTableParameter parameter, String categoryId, String sreleaseYear, String ereleaseYear,String seriesName) {
        PageRequest pageRequest = parameter.getPageRequest();
        Finder finder = Finder.create("select m from MediaSeries m where m.delFlag=:delFlag and m.status=:status ")
                .setParam("delFlag", MediaSeries.DEL_FLAG_NORMAL).setParam("status", "3");
        finder.search("releaseYear", sreleaseYear, Finder.SearchType.GE);
        finder.search("releaseYear", ereleaseYear, Finder.SearchType.LE);
        finder.search("seriesName", seriesName, Finder.SearchType.LIKE);

        List<String> categoryList = new ArrayList();
        for (String str : categoryId.split(",")) {
            categoryList.add(str);
        }
        if (!Strings.isBlank(categoryId)) {
            finder.search("categoryId", categoryList, Finder.SearchType.IN);
        }
        Page<MediaSeries> page = verifyMapper.find(finder, pageRequest);
        return new PageTable<>(page);
    }

    @Transactional
    public void update(String[] ids, String reason, String status) {
        List<VerifyRecord> verifyRecordList=new ArrayList<>();
        //修改节目集审核状态
        String publish = status.equals("1") ?"1":"0";

        for (String str : ids) {
            verifyMapper.updateMediaseries(status,str,publish);
           // verifyMapper.updateByUpdate(Parameter.create().insert("status", status), Parameter.create().insert("id", str).insert("publish",publish));
            VerifyRecord verifyRecord = new VerifyRecord();
            verifyRecord.setReason(reason);
            verifyRecord.setSeriesId(str);
            verifyRecordList.add(verifyRecord);
        }
        //添加审核记录
        verifyRecordMapper.saveAll(verifyRecordList);

    }
}
